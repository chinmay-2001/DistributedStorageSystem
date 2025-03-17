package com.example.DistributedStorageSystem.Modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileMetadata {

    @Id
    @GeneratedValue
    @JsonProperty("id")
    private UUID id;

    @Column(nullable = false)
    private String  filename;

    @Column(nullable = false)
    private  int totalChunk;

    @Column(nullable = false)
    private  int FileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileStatus fileStatus=FileStatus.COMPLETED;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt= LocalDateTime.now();

    @Column(nullable = false)
    private String filetype;

    @Column
    private  LocalDateTime updatedAt=LocalDateTime.now();

    public AppUser getAppUser() {
        return appUser;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @PreUpdate
    protected  void onUpdate(){
        updatedAt=LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getTotalChunk() {
        return totalChunk;
    }

    public void setTotalChunk(int totalChunk) {
        this.totalChunk = totalChunk;
    }

    public int getFileSize() {
        return FileSize;
    }

    public void setFileSize(int fileSize) {
        FileSize = fileSize;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


}
