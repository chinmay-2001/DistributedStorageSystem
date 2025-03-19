package com.example.DistributedStorageSystem.Exception;

public  class ChunkProcessingException extends Exception {
    private final String chunkKey;

    public ChunkProcessingException(String chunkKey,String message){
        super(message);
        this.chunkKey=chunkKey;
    }

    public  String getChunkKey(){
        return chunkKey;
    }
}