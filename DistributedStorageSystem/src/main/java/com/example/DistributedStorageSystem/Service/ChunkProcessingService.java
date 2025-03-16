package com.example.DistributedStorageSystem.Service;

import com.example.DistributedStorageSystem.Modal.FileChunk;
import com.example.DistributedStorageSystem.Modal.FileMetadata;
import com.example.DistributedStorageSystem.Repo.ChunkRepo;
import com.example.DistributedStorageSystem.Repo.FileMetadataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChunkProcessingService {

    @Autowired
    ChunkRepo chunkRepo;

    @Autowired
    FileMetadataRepo fileMetadataRepo;

    public FileChunk saveChunk(String fileId, String chunkUrl, Integer chunkIndex, Integer chunkSize){
        FileChunk fileChunk = new FileChunk();
        fileChunk.setChunkNumber(chunkIndex);
        fileChunk.setChunkSize(chunkSize);
        fileChunk.setChunkUrl(chunkUrl);
        FileMetadata file = fileMetadataRepo.findById(UUID.fromString(fileId)).orElseThrow(()->new RuntimeException("File Metadata not found for Id:"+fileId));
        fileChunk.setFile(file);
        return this.chunkRepo.save(fileChunk);
    }
}
