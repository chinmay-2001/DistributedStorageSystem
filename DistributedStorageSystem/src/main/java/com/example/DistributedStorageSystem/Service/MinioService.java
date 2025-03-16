package com.example.DistributedStorageSystem.Service;

import com.example.DistributedStorageSystem.Configuration.MiniConfig;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import kotlin.jvm.Throws;
import org.apache.coyote.BadRequestException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

@Service
public class MinioService {
    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    private final Tika tika = new Tika();


    public String uploadFile(File file,String chunkKey) throws Exception {
        if (file == null || !file.exists() || file.length() == 0) {
            throw new IllegalArgumentException("Invalid file. Please provide a valid file.");
        }
        try(InputStream inputStream = new FileInputStream(file)) {
            System.out.println("fileType:"+Files.probeContentType(file.toPath()));
            String contentType = tika.detect(file);
            String chunkObjectName=bucketName+chunkKey;
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(chunkObjectName)
                            .stream(inputStream, file.length(), -1)
                            .contentType(contentType)
                            .build()
            );

            String PermanaentUrl="http://localhost:9000/"+bucketName+"/"+chunkObjectName;

            return PermanaentUrl;
        }
    }

}
