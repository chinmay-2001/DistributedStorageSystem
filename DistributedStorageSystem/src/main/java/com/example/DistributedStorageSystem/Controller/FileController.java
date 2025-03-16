package com.example.DistributedStorageSystem.Controller;

import com.example.DistributedStorageSystem.Configuration.QueueStoreSpace;
import com.example.DistributedStorageSystem.Modal.FileMetadata;
import com.example.DistributedStorageSystem.Modal.FileMetadataDTO;
import com.example.DistributedStorageSystem.Service.ChunkService;
import com.example.DistributedStorageSystem.Service.FileService;
import com.example.DistributedStorageSystem.Utils.ConvertMultiPartToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/files")

public class FileController {
    @Autowired
    FileService fileService;

    @Autowired
    ChunkService chunkService;

    @PostMapping("/FileMetadata")
    public ResponseEntity<?> uploadFileMetaData(@RequestBody FileMetadataDTO fileMetadata){
        try{
            System.out.println("fileSize:"+ fileMetadata.getFileSize());
            System.out.println(fileMetadata);
            FileMetadata metadata=fileService.uploadFileMetaData(fileMetadata);
            return new ResponseEntity<>(metadata,HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/file-metadata/{userId}")
    public ResponseEntity<?> getFileMetadata(@PathVariable int userId){
        try{
            return new ResponseEntity<>(fileService.getFileMetaDataByUserId(userId),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/chunk-upload")
    public CompletableFuture<ResponseEntity<QueueStoreSpace.ChunkResponse>> chunkUpload(@RequestParam("file") MultipartFile  file, @RequestParam("fileId")String fileId, @RequestParam("chunkIndex") int chunkIndex) throws IOException {

        try {
            File newFile=ConvertMultiPartToFile.convertMultipartFileToFile(file);
            return chunkService.uploadChunk(newFile, fileId,chunkIndex)
                    .thenApply((result)->ResponseEntity.ok(result))
                    .exceptionally(exception->{
                            QueueStoreSpace.ChunkResponse errorResponse=new QueueStoreSpace.ChunkResponse(null,null,exception.getMessage());
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                        }
                    );
        } catch (Exception e) {
            QueueStoreSpace.ChunkResponse errorResponse = new QueueStoreSpace.ChunkResponse(null, null, "File processing error: " + e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
        }
    }

    @PatchMapping("/confirm-upload/{fileId}")
    public ResponseEntity<?> confirmFileUploadCompletion(@PathVariable UUID fileId){
        try {
            System.out.println(fileId);
            return new ResponseEntity<>(fileService.confirmFileUploadCompletion(fileId), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
