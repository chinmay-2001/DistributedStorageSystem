package com.example.DistributedStorageSystem.Controller;

import com.example.DistributedStorageSystem.Service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class MinioController {

    @Autowired
    MinioService minioService;

    @GetMapping("/hi")
    public  ResponseEntity<String>getHi(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String > uploadFile(@RequestParam("file") MultipartFile file){
        try{
//            String response= minioService.uploadFile(file);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
