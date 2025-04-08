package com.example.DistributedStorageSystem.Service;

import com.example.DistributedStorageSystem.Utils.ExtractObjectNameFromUrl;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService {
    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.url}")
    private String customeHost;

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

    public GetObjectResponse getObject(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    public String generatePresignedUrl(String url) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String Objectname=ExtractObjectNameFromUrl.extractObjectNameFromUrl(url);

        String presignedObjectUrl= minioClient
                .getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucketName)
                                .object(Objectname)
                                .expiry(600)
                                .build()
                );
        URL parsedurl=new URL(presignedObjectUrl);
        String modifiedurl=customeHost+ parsedurl.getFile();
        return modifiedurl;
    }
}
