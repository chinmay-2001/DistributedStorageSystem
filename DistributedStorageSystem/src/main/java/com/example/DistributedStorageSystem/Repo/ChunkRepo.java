package com.example.DistributedStorageSystem.Repo;

import com.example.DistributedStorageSystem.Modal.FileChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChunkRepo  extends JpaRepository<FileChunk,Integer> {
    @Query("SELECT p.chunkUrl FROM FileChunk p WHERE p.file.id= :fileId ORDER BY p.chunkNumber ASC")
    List<String> getChunkUrls(UUID fileId);
}
