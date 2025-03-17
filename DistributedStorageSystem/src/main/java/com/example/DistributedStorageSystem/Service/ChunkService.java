package com.example.DistributedStorageSystem.Service;

import com.example.DistributedStorageSystem.Configuration.QueueStoreServiceConfig;
import com.example.DistributedStorageSystem.Configuration.QueueStoreSpace;
import com.example.DistributedStorageSystem.Modal.FileChunk;
import com.example.DistributedStorageSystem.Modal.FileMetadata;
import com.example.DistributedStorageSystem.Repo.ChunkRepo;
import com.example.DistributedStorageSystem.Repo.FileMetadataRepo;
import org.bouncycastle.util.StoreException;
import org.hibernate.Internal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class ChunkService {

    @Autowired
    QueueStoreSpace queueStoreSpace;

    @Autowired
    ChunkRepo chunkRepo;

    @Autowired
    FileMetadataRepo fileMetadataRepo;

    @Async
    public CompletableFuture<QueueStoreSpace.ChunkResponse> uploadChunk(File file, String fileId,int chunkIndex) throws Exception {
        try {
            return queueStoreSpace.put(file,fileId,chunkIndex,(int)file.length());
        }catch (StoreException s){
            System.out.println("GetMessage:"+s.getMessage());
            throw new RuntimeException(s.getMessage());
        }
    }

    public List<String> downloadFile(UUID fileId) {
        return chunkRepo.getChunkUrls(fileId);
    }
}




