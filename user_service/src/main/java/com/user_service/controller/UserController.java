package com.user_service.controller;

import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;
import com.user_service.service.UserService;
import com.user_service.service.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private  UserServiceImp userServiceImp;

    @Autowired
    private UserService userService;


    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody UserRequest request) {

        return new ResponseEntity<>(
                userServiceImp.createUser(request),
                HttpStatus.CREATED
        );
    }


    public  ResponseEntity<UserResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.getUser(id));

    }


}