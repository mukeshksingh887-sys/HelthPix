package com.auth_service.controller;

import com.auth_service.dto.*;
import com.auth_service.repository.RefreshTokenRepository;
import com.auth_service.service.AuthService;
import com.auth_service.service.AuthServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
   private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        LoginResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(
                authService.refreshToken(request)
        );
    }


    @PostMapping("/logout/{userId}")
    public ResponseEntity<String> logout(
            @PathVariable Long userId) {

//        refreshTokenRepository.deleteByUserId(userId);

        authService.logout(userId);
        return ResponseEntity.ok("Logged Out");
    }


    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(
            @RequestBody OtpRequest request) {

        authService.sendOtp(request.getEmail());

        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Boolean> verifyOtp(
            @RequestBody VerifyOtpRequest request) {

        return ResponseEntity.ok(
                authService.verifyOtp(
                        request.getEmail(),
                        request.getOtp()
                )
        );
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        authService.forgotPassword(request.getEmail());

        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(request);

        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam Long userId,
            @RequestBody ChangePasswordRequest request) {

        authService.changePassword(userId, request);

        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(
            @RequestHeader("Authorization") String token) {

        token = token.replace("Bearer ", "");

        return ResponseEntity.ok(
                authService.validateToken(token)
        );
    }

    @PostMapping("/revoke-token")
    public ResponseEntity<String> revokeToken(
            @RequestHeader("Authorization") String token) {

        token = token.replace("Bearer ", "");

        authService.revokeToken(token);

        return ResponseEntity.ok("Token revoked successfully");
    }

    @PostMapping("/revoke-all-tokens")
    public ResponseEntity<String> revokeAllTokens(
            @RequestParam Long userId) {

        authService.revokeAllTokens(userId);

        return ResponseEntity.ok("All tokens revoked successfully");
    }


}
