package com.example.DistributedStorageSystem.Modal;

public class ChunkResponse {
    private final boolean success;
    private final String errorMessage;

    public ChunkResponse(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
