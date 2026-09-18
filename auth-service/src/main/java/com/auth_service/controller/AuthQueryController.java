package com.auth_service.controller;


import com.auth_service.dto.AuthUserResponse;
import com.auth_service.entity.UserType;
import com.auth_service.service.AuthQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthQueryController {

    private final AuthQueryService authQueryService;

    @GetMapping("/me")
    public AuthUserResponse me(
            @RequestHeader("X-User-Id")
            Long userId) {

        return authQueryService
                .getCurrentUser(userId);
    }

    @GetMapping("/user/{userId}")
    public AuthUserResponse getByUserId(
            @PathVariable Long userId) {

        return authQueryService
                .getByUserId(userId);
    }

    @GetMapping("/email/{email}")
    public AuthUserResponse getByEmail(
            @PathVariable String email) {

        return authQueryService
                .getByEmail(email);
    }

    @GetMapping("/role/{role}")
    public List<AuthUserResponse> getByRole(
            @PathVariable UserType role) {

        return authQueryService
                .getByRole(role);
    }

//    @GetMapping("/status/{status}")
//    public List<AuthUserResponse> getByStatus(
//            @PathVariable UserStatus status) {
//
//        return authQueryService
//                .getByStatus(status);
//    }
}