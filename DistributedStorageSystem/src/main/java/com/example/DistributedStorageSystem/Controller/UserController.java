package com.example.DistributedStorageSystem.Controller;

import com.example.DistributedStorageSystem.Modal.AppUser;
import com.example.DistributedStorageSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> requestBody ){
        String email = requestBody.get("email");
        String password = requestBody.get("password");
        AppUser appUser=userService.verifyUser(email, password);
        return new ResponseEntity<>(appUser, HttpStatus.OK);
}

    @RequestMapping("/signUp")
    public ResponseEntity<?> signup(@RequestBody AppUser appUsers) {
        AppUser appUser = userService.createUser(appUsers);
        return new ResponseEntity<>(appUser, HttpStatus.OK);
    }
}
