package com.example.DistributedStorageSystem.Service;

import com.example.DistributedStorageSystem.Modal.AppUser;
import com.example.DistributedStorageSystem.Repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    AppUserRepo repo;

    public AppUser  createUser(AppUser user) {
        AppUser users=repo.save(user);
        return users;
    }

    public AppUser verifyUser(String email, String password) {
        return  repo.findByEmailandPassword(email,password);
    }
}

