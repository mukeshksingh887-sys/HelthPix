package com.user_service.controller;

import com.user_service.dto.UserResponse;
import com.user_service.entity.User;
import com.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {
    @Autowired
    private final UserService userService;

    @GetMapping("/email/{email}")
    public UserResponse getByEmail(
            @PathVariable String email) {

        return userService.getByEmail(email);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        UserResponse user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }


     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteUserById(
             @PathVariable Long id) {

         userService.deleteUser(id);

         return ResponseEntity.ok(
                 "Doctor deleted successfully");
     }
//    @GetMapping("/{id}")
//    public UserResponse getUserById(@PathVariable Long id){
//        return userService.getUserById(id);
//    }
}