package com.example.DistributedStorageSystem.Repo;

import com.example.DistributedStorageSystem.Modal.FileChunk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChunkRepo  extends JpaRepository<FileChunk,Integer> {

}
