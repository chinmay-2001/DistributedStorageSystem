package com.example.DistributedStorageSystem.Configuration;

import org.bouncycastle.util.StoreException;

import java.util.concurrent.CompletableFuture;

public interface StoreSpace<T> {

    /**
            * Adds an item to the store.
            * @param item The item to be added.
            * @throws StoreException If an error occurs while adding.
     */

    CompletableFuture<QueueStoreSpace.ChunkResponse> put(T item, String fileId, Integer chunkIndex,Integer fileSize) throws StoreException;

    T take()  throws StoreException;

    T peek() throws StoreException;

    boolean isEmpty() ;

    boolean hasItem() ;

    void close() ;

    boolean isClosed() ;

    void  setEndDelivery() ;
}
