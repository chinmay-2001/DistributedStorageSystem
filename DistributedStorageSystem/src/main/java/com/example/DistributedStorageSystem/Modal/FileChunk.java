package com.example.DistributedStorageSystem.Modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileChunk {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private FileMetadata file;

    @Column(nullable = false)
    private int chunkNumber;

    @Column(nullable = false)
    private long chunkSize;

    @Column(nullable = false)
    private String chunkUrl;

    @Column(nullable = false)
    private ChunkStatus status = ChunkStatus.PENDING;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
