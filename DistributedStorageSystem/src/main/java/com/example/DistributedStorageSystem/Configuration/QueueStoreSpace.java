package com.example.DistributedStorageSystem.Configuration;

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
    public CompletableFuture<ChunkResponse> put(T item,String fileId,Integer chunkIndex,Integer fileSize) throws StoreException {
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
                String chunkkey = chunkData.chunkKey;
                T item = (T) chunkData.file;
                String fileId=chunkData.fileId;
                Integer chunkIndex=chunkData.chunkIndex;
                Integer  chunkSize=chunkData.chunkSize;


                String chunkUrl = minioService.uploadFile((File) item,chunkkey);

                // store  the chunkUrl in the store
                this.chunkProcessorService.saveChunk(fileId,chunkUrl,chunkIndex,chunkSize);

                CompletableFuture<ChunkResponse> future = chunkFuture.remove(chunkkey);
                if (future != null) {
                    future.complete(new ChunkResponse(chunkkey, chunkUrl, null));
                }


                System.out.println(Thread.currentThread().getName() + " - Success: " + item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e){
                if(e.getMessage()!=null){
                    String chunkKey="UNKNOWN";
                    if(e instanceof ChunkProcessingException){
                        chunkKey=((ChunkProcessingException) e).getChunkKey();
                    }

                    CompletableFuture<ChunkResponse> future =chunkFuture.remove(chunkKey);


                    if(future!=null){
                        future.complete(new ChunkResponse(chunkKey,null,e.getMessage()));
                    }
                }
            }
        }
    }

    private static class ChunkData<T> {
        T file;
        String chunkKey;
        String fileId;
        Integer chunkSize;
        Integer chunkIndex;

        public ChunkData(T file, String chunkKey, String fileId, Integer chunkSize, Integer chunkIndex) {
            this.file = file;
            this.chunkKey = chunkKey;
            this.fileId = fileId;
            this.chunkSize = chunkSize;
            this.chunkIndex = chunkIndex;
        }
    }


    public static class ChunkResponse {
        private final String chunkKey;
        private final String chunkUrl;
        private final String error;

        public ChunkResponse(String chunkKey, String chunkUrl, String error) {
            this.chunkKey = chunkKey;
            this.chunkUrl = chunkUrl;
            this.error = error;
        }

        public String getChunkKey() {
            return chunkKey;
        }

        public String getChunkUrl() {
            return chunkUrl;
        }

        public String getError() {
            return error;
        }

        public boolean isSuccess() {
            return error == null;
        }
    }

    public static class ChunkProcessingException extends Exception{
        private final String chunkKey;

        public ChunkProcessingException(String chunkKey,String message){
            super(message);
            this.chunkKey=chunkKey;
        }

        public  String getChunkKey(){
            return chunkKey;
        }
    }
}