package com.example.DistributedStorageSystem.Repo;

import com.example.DistributedStorageSystem.Modal.FileMetadata;
import com.example.DistributedStorageSystem.Modal.FileMetadataDTO;
import com.example.DistributedStorageSystem.Modal.FileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileMetadataRepo  extends JpaRepository<FileMetadata, UUID> {

    @Query("SELECT p FROM FileMetadata p WHERE p.appUser.id = :userId AND p.fileStatus= :status ")
    List<?> getByUserId(int userId,FileStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE FileMetadata f SET f.fileStatus= :status WHERE f.id= :fileId")
    int updateFileMetadata(UUID fileId, FileStatus status);
}
