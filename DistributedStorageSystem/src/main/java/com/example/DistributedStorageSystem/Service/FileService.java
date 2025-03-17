package com.example.DistributedStorageSystem.Service;

import com.example.DistributedStorageSystem.Modal.AppUser;
import com.example.DistributedStorageSystem.Modal.FileMetadata;
import com.example.DistributedStorageSystem.Modal.FileMetadataDTO;
import com.example.DistributedStorageSystem.Modal.FileStatus;
import com.example.DistributedStorageSystem.Repo.AppUserRepo;
import com.example.DistributedStorageSystem.Repo.ChunkRepo;
import com.example.DistributedStorageSystem.Repo.FileMetadataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    FileMetadataRepo fileMetadataRepo;

    @Autowired
    AppUserRepo appUserRepo;

    @Autowired
    ChunkService chunkService;

    public FileMetadata uploadFileMetaData(FileMetadataDTO fileMetadataDTO) {
        FileMetadata fileMetadata= new FileMetadata();
        fileMetadata.setFileSize(fileMetadataDTO.getFileSize());
        fileMetadata.setFilename(fileMetadataDTO.getFilename());
        fileMetadata.setTotalChunk(fileMetadataDTO.getTotalchunk());
        fileMetadata.setFileStatus(FileStatus.UPLOADING);
        fileMetadata.setFiletype(fileMetadataDTO.getFileType());
        AppUser appUser=appUserRepo.findById(fileMetadataDTO.getUserId()).orElseThrow(()-> new RuntimeException("userId not found With User Id:"+fileMetadataDTO.getUserId()));
        fileMetadata.setAppUser(appUser);

        return fileMetadataRepo.save(fileMetadata);
    }

    public List<?> getFileMetaDataByUserId(int userId) {
        List<?> listFileMetadata=fileMetadataRepo.getByUserId(userId,FileStatus.COMPLETED);
        return listFileMetadata;
    }

    public int confirmFileUploadCompletion(UUID fileId) {
        return fileMetadataRepo.updateFileMetadata(fileId, FileStatus.COMPLETED);
    }


}
