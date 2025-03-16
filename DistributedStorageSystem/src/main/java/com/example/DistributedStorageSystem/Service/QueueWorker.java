package com.example.DistributedStorageSystem.Service;

import com.example.DistributedStorageSystem.Service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class QueueWorker<T> {
    private BlockingQueue<T> queue;
    private int maxRetries;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    @Autowired
    MinioService minioService;


//    public QueueWorker() {
//        this.queue = queue;
//        this.maxRetries = maxRetries;
//    }
//
//    public void run() {
//        while (true) {
//            try {
//                T item = queue.take(); // Blocks until an item is available
//                processChunk(item);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                break; // Stop thread if interrupted
//            }
//        }
//    }
//
//    public void processChunk(T item){
//        try{
//            minioService.uploadFile(item);
//            return;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


}
