package com.user_service.controller;

import com.user_service.dto.UserResponse;
import com.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


//    @GetMapping("/{id}")
//    public UserResponse getUserById(@PathVariable Long id){
//        return userService.getUserById(id);
//    }
}