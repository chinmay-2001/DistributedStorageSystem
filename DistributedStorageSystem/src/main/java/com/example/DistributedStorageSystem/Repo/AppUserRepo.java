package com.example.DistributedStorageSystem.Repo;

import com.example.DistributedStorageSystem.Modal.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppUserRepo extends JpaRepository<AppUser,Integer> {
    @Query("SELECT u FROM AppUser u WHERE u.email = :email AND u.password = :password")
    AppUser findByEmailandPassword(String email, String password);
}
