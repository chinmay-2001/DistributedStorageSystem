package com.example.DistributedStorageSystem.Controller;

import com.example.DistributedStorageSystem.Configuration.QueueStoreSpace;
import com.example.DistributedStorageSystem.Exception.ResourceNotFoundException;
import com.example.DistributedStorageSystem.Exception.handleRuntimeException;
import com.example.DistributedStorageSystem.Modal.FileMetadata;
import com.example.DistributedStorageSystem.Modal.FileMetadataDTO;
import com.example.DistributedStorageSystem.Service.ChunkService;
import com.example.DistributedStorageSystem.Service.FileService;
import com.example.DistributedStorageSystem.Service.MinioService;
import com.example.DistributedStorageSystem.Utils.ContentTypeUtils;
import com.example.DistributedStorageSystem.Utils.ConvertMultiPartToFile;
import com.example.DistributedStorageSystem.Utils.ExtractObjectNameFromUrl;
import com.example.DistributedStorageSystem.Utils.MimetypeUtil;
import io.minio.GetObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import org.hibernate.engine.jdbc.ReaderInputStream;
import org.simpleframework.xml.core.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.*;
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
    public ResponseEntity<?> uploadFileMetaData( @RequestBody FileMetadataDTO fileMetadata){
        FileMetadata metadata=fileService.uploadFileMetaData(fileMetadata);
        return new ResponseEntity<>(metadata,HttpStatus.OK);
    }

    @GetMapping("/file-metadata/{userId}")
    public ResponseEntity<?> getFileMetadata(@PathVariable int userId){
        return new ResponseEntity<>(fileService.getFileMetaDataByUserId(userId),HttpStatus.OK);
    }

    @PostMapping("/chunk-upload")
    public CompletableFuture<ResponseEntity<?>> chunkUpload(@RequestParam("file") MultipartFile  file
            , @RequestParam("fileId")String fileId
            , @RequestParam("chunkIndex") int chunkIndex) throws Exception {


            File newFile=ConvertMultiPartToFile.convertMultipartFileToFile(file);
            return chunkService.uploadChunk(newFile, fileId,chunkIndex)
                    .thenApply(ResponseEntity::ok);
    }

    @PatchMapping("/confirm-upload/{fileId}")
    public ResponseEntity<?> confirmFileUploadCompletion(@PathVariable UUID fileId){
        return new ResponseEntity<>(fileService.confirmFileUploadCompletion(fileId), HttpStatus.OK);
    }

    @GetMapping("/download-file/{fileId}/{fileType}")
    public ResponseEntity<?> downloadFile(@PathVariable UUID fileId, @PathVariable String fileType) throws Exception {
        List<String> chunkUrls=chunkService.downloadFile(fileId);
        if(chunkUrls.isEmpty()){
            throw new ResourceNotFoundException("File not found");
        }

        ArrayList<String> presignedUrls =chunkService.generatePresignedUrls(chunkUrls);
        return new ResponseEntity<>(presignedUrls,HttpStatus.OK);
//        String contentType=MimetypeUtil.getMimeType(fileType);
//        System.out.println("ContentType:"+contentType);
//        fileType= ContentTypeUtils.getFileType(fileType);
//
//        StreamingResponseBody streamingResponseBody= outputStream -> {
//            for (String chunkurl:chunkUrls){
//                String objectName=ExtractObjectNameFromUrl.extractObjectNameFromUrl(chunkurl);
//
//                try(GetObjectResponse getObjectResponse =minioService.getObject(objectName)) {
//                    System.out.println(getObjectResponse);
//                    byte[] buffer = new byte[65536];
//                    int bytesRead;
//                    while ((bytesRead = getObjectResponse.read(buffer)) != -1) {
//                        outputStream.write(buffer, 0, bytesRead);
//                        outputStream.flush();
//                    }
//                }catch (Exception e){
//                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error Retreiving Files");
//                }
//            }
//        };
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"downloaded-file."+fileType+"\"")
//                .contentType(MediaType.parseMediaType(contentType))
//                .body(streamingResponseBody);

    }
}
