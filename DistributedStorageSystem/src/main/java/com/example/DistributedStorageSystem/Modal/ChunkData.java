package com.example.DistributedStorageSystem.Modal;

public  class ChunkData<T> {
     T file;
     String chunkKey;
     String fileId;
     Integer chunkSize;
     Integer chunkIndex;

    public T getFile() {
        return file;
    }

    public void setFile(T file) {
        this.file = file;
    }

    public String getChunkKey() {
        return chunkKey;
    }

    public void setChunkKey(String chunkKey) {
        this.chunkKey = chunkKey;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Integer getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(Integer chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public ChunkData(T file, String chunkKey, String fileId, Integer chunkSize, Integer chunkIndex) {
        this.file = file;
        this.chunkKey = chunkKey;
        this.fileId = fileId;
        this.chunkSize = chunkSize;
        this.chunkIndex = chunkIndex;
    }
}

