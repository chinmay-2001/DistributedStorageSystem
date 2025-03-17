package com.example.DistributedStorageSystem.Controller;

import com.example.DistributedStorageSystem.Configuration.QueueStoreSpace;
import com.example.DistributedStorageSystem.Modal.FileMetadata;
import com.example.DistributedStorageSystem.Modal.FileMetadataDTO;
import com.example.DistributedStorageSystem.Service.ChunkService;
import com.example.DistributedStorageSystem.Service.FileService;
import com.example.DistributedStorageSystem.Service.MinioService;
import com.example.DistributedStorageSystem.Utils.ConvertMultiPartToFile;
import com.example.DistributedStorageSystem.Utils.ExtractObjectNameFromUrl;
import com.example.DistributedStorageSystem.Utils.MimetypeUtil;
import io.minio.GetObjectResponse;
import org.hibernate.engine.jdbc.ReaderInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@RestController
@RequestMapping("/files")

public class FileController {
    @Autowired
    FileService fileService;

    @Autowired
    ChunkService chunkService;

    @Autowired
    MinioService minioService;

    @PostMapping("/FileMetadata")
    public ResponseEntity<?> uploadFileMetaData(@RequestBody FileMetadataDTO fileMetadata){
        try{
            FileMetadata metadata=fileService.uploadFileMetaData(fileMetadata);
            return new ResponseEntity<>(metadata,HttpStatus.OK);
        }catch (Exception e){
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
            return new ResponseEntity<>(fileService.confirmFileUploadCompletion(fileId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download-file/{fileId}/{fileType}")
    public ResponseEntity<StreamingResponseBody> downloadFile(@PathVariable UUID fileId, @PathVariable String fileType){
        List<String> chunkUrls=chunkService.downloadFile(fileId);
        if(chunkUrls.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String contentType=MimetypeUtil.getMimeType(fileType);



        StreamingResponseBody streamingResponseBody= outputStream -> {
            try {
                for (String chunkurl:chunkUrls){
                    String objectName=ExtractObjectNameFromUrl.extractObjectNameFromUrl(chunkurl);
                    GetObjectResponse getObjectResponse =minioService.getObject(objectName);
                    System.out.println(getObjectResponse);
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead= getObjectResponse.read(buffer))!=-1){
                        outputStream.write(buffer,0,bytesRead);
                        outputStream.flush();
                    }
                    getObjectResponse.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        System.out.println(contentType);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"downloaded-file."+fileType+"\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(streamingResponseBody);

    }

}
