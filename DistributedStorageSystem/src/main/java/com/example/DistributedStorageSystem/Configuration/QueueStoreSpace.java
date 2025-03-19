package com.example.DistributedStorageSystem.Configuration;

import com.example.DistributedStorageSystem.Exception.ChunkProcessingException;
import com.example.DistributedStorageSystem.Modal.ChunkData;
import com.example.DistributedStorageSystem.Modal.ChunkResponse;
import com.example.DistributedStorageSystem.Service.ChunkProcessingService;

import com.example.DistributedStorageSystem.Service.MinioService;
import org.bouncycastle.util.StoreException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueStoreSpace<T> implements StoreSpace<T> {

    protected BlockingQueue<T> queue;
    private int nbProviders = 1;
    // nbEndDelivery is AtomicInteger type for manage better the incrementation in multithreading
    private AtomicInteger nbEndDelivery = new AtomicInteger(0);
    private boolean endDelivery = false;
    private boolean closed = false;
    private final ExecutorService executor;
    private final ConcurrentHashMap<String, CompletableFuture<ChunkResponse>> chunkFuture = new ConcurrentHashMap<>();


    @Autowired
    MinioService minioService;

    @Autowired
    ChunkProcessingService chunkProcessorService;

    protected QueueStoreSpace(Builder<T> b) {
        this.nbProviders = b.nbProviders;
        this.queue = new ArrayBlockingQueue<>(b.queueSize);
        this.executor = Executors.newFixedThreadPool(3);
        startWorkers();
    }

    @Override
    public void setEndDelivery() {
        if (nbEndDelivery.incrementAndGet() == nbProviders) {
            this.endDelivery = true;
        }
    }

    void startWorkers(){
        for (int i = 0; i < 3; i++) {
            executor.submit(()->processChunks());
        }
    }

    public static class Builder<T> {
        private int queueSize;
        private int nbProviders = 1;

        public Builder(int queueSize) {
            this.queueSize = queueSize;
        }

        public Builder<T> withNbProviders(int nbProviders) {
            this.nbProviders = nbProviders;
            return this;
        }

        public QueueStoreSpace<T> build() {
            return new QueueStoreSpace<>(this);
        }
    }

    @Override
    public CompletableFuture<ChunkResponse> put(T item, String fileId, Integer chunkIndex, Integer fileSize) throws StoreException {
        try {

            String chunkKey=fileId+Integer.toString(chunkIndex);
            CompletableFuture<ChunkResponse> future = new CompletableFuture<>();

            queue.put((T) new ChunkData(item,chunkKey,fileId,fileSize,chunkIndex));

            chunkFuture.put(chunkKey,future);

            return future;
        } catch (StoreException e) {
            throw e;
        } catch (Exception e) {
            throw new StoreException("Unable to add item in the queue", e);
        }
    }

    @Override
    public T take() throws StoreException {
        try {
            if (closed) {
                throw new StoreException("Store is closed",new Throwable());
            }
            return queue.take();

        } catch (StoreException e) {
            throw e;
        } catch (Exception e) {
            throw new StoreException("Unable to take item from queue", e);
        }

    }

    @Override
    public T peek() throws StoreException {
        try {
            if (closed) {
                throw new StoreException("Store is closed",new Throwable("Store is closed"));
            }
            return queue.peek();

        } catch (StoreException e) {
            throw e;
        } catch (Exception e) {
            throw new StoreException("Unable to peek item from queue", e);
        }
    }

    @Override
    public boolean isEmpty() {
        return (endDelivery && queue.isEmpty());
    }

    @Override
    public boolean hasItem() {
        return !queue.isEmpty();
    }

    @Override
    public void close() {
        closed = true;
        executor.shutdown();
        queue.clear();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    private void processChunks() {
        while (!closed) {
            try {
                ChunkData chunkData = (ChunkData) take();
                processChunk(chunkData);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e){
              handleExceptionProcessing(e);
            }
        }
    }

    public void processChunk(ChunkData chunkData) throws Exception {
        String chunkkey = chunkData.getChunkKey();
        T item = (T) chunkData.getFile();
        String fileId=chunkData.getFileId();
        Integer chunkIndex=chunkData.getChunkIndex();
        Integer  chunkSize=chunkData.getChunkSize();

        String chunkUrl=uploadChunkToMinio(item,chunkkey);
        saveChunkMetadata(fileId,chunkUrl,chunkIndex,chunkSize);

        CompletableFuture<ChunkResponse> future = chunkFuture.remove(chunkkey);
        if(future!=null){
            future.complete(new ChunkResponse( true,null));
        }
    }

    private void saveChunkMetadata(String fileId, String chunkUrl, Integer chunkIndex, Integer chunkSize) throws ChunkProcessingException {
        this.chunkProcessorService.saveChunk(fileId,chunkUrl,chunkIndex,chunkSize);
    }

    public  String uploadChunkToMinio(T item,String chunkkey) throws Exception {
       String chunkUrl= minioService.uploadFile((File) item,chunkkey);
       if(chunkUrl==null){
           throw new ChunkProcessingException(chunkkey,"Failed to upload Chunk to Minio");
       }
       return chunkUrl;
    }


    void handleExceptionProcessing(Exception e){
        if(e.getMessage()!=null){
            String chunkKey="UNKNOWN";
            if(e instanceof ChunkProcessingException){
                chunkKey=((ChunkProcessingException) e).getChunkKey();
            }

            CompletableFuture<ChunkResponse> future =chunkFuture.remove(chunkKey);


            if(future!=null){
                future.completeExceptionally(e);
            }
        }
    }
}