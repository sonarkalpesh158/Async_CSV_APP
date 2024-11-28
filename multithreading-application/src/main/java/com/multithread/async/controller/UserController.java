package com.multithread.async.controller;

import com.multithread.async.model.User;
import com.multithread.async.service.UserService;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users",consumes="multipart/form-data",produces = "application/json")
    public ResponseEntity saveUsers(@RequestPart("files") MultipartFile[] files) throws Exception {

        for (MultipartFile file:files){
            userService.saveUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/users",produces = "application/json")
    public CompletableFuture<ResponseEntity> getUsers(){
        return userService.getAllUsers().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/getUsersByThread",produces = "application/json")
    public ResponseEntity getUsersByThread(){
        CompletableFuture<List<User>> allUsers1 = userService.getAllUsers();
        CompletableFuture<List<User>> allUsers2 = userService.getAllUsers();
        CompletableFuture<List<User>> allUsers3 = userService.getAllUsers();
        CompletableFuture.allOf(allUsers1,allUsers2,allUsers3).join();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
