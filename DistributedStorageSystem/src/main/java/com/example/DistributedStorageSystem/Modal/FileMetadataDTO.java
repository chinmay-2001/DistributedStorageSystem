package com.example.DistributedStorageSystem.Modal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadataDTO {
    @Override
    public String toString() {
        return "FileMetadataDTO{" +
                "filename='" + filename + '\'' +
                ", totalchunk=" + totalchunk +
                ", fileSize=" + fileSize +
                ", userId=" + userId +
                '}';
    }

    private String filename;
    private int totalchunk;
    private int fileSize;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int userId;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getTotalchunk() {
        return totalchunk;
    }

    public void setTotalchunk(int totalchunk) {
        this.totalchunk = totalchunk;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }


}
