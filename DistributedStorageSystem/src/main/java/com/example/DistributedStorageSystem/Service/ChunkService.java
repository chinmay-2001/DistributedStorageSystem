package com.example.DistributedStorageSystem.Service;

import com.example.DistributedStorageSystem.Configuration.QueueStoreServiceConfig;
import com.example.DistributedStorageSystem.Configuration.QueueStoreSpace;
import com.example.DistributedStorageSystem.Modal.FileChunk;
import com.example.DistributedStorageSystem.Modal.FileMetadata;
import com.example.DistributedStorageSystem.Repo.ChunkRepo;
import com.example.DistributedStorageSystem.Repo.FileMetadataRepo;
import io.minio.errors.*;
import org.bouncycastle.util.StoreException;
import org.hibernate.Internal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

    @Autowired
    MinioService minioService;

    @Async
    public CompletableFuture<?> uploadChunk(File file, String fileId,int chunkIndex) throws Exception {
        try {
            return queueStoreSpace.put(file,fileId,chunkIndex,(int)file.length());
        }catch (StoreException s){
            throw new RuntimeException(s.getMessage());
        }
    }

    public List<String> downloadFile(UUID fileId) {
        return chunkRepo.getChunkUrls(fileId);
    }

    public ArrayList<String> generatePresignedUrls(List<String> urls) throws Exception {
        ArrayList<String> presignedUrl=new ArrayList<>();
        for(String url:urls) {
            try {
                presignedUrl.add(minioService.generatePresignedUrl(url));
            } catch (ServerException e) {
                throw new RuntimeException("Error generating presigned URL for: " + url, e);
            }
        }
        return presignedUrl;
    }
}




