package com.auth_service.controller;



import com.auth_service.dto.AuthUserRequest;
import com.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/auth")
@RequiredArgsConstructor
public class AuthInternalController {
    @Autowired
    private final AuthService authService;

    @PostMapping("/users")
    public ResponseEntity<String> createAuthUser(
            @RequestBody AuthUserRequest request) {

        authService.createAuthUser(request);

        return ResponseEntity.ok(
                "Auth User Created Successfully"
        );
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteAuthUser(
            @PathVariable Long userId) {

        authService.deleteAuthUser(userId);

        return ResponseEntity.ok("Auth User Deleted");
    }
}