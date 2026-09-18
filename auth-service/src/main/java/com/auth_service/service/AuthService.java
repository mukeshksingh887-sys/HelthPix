package com.auth_service.service;

import com.auth_service.dto.*;
import jakarta.transaction.Transactional;

public interface AuthService {
    void createAuthUser(AuthUserRequest request);

    void deleteAuthUser(Long userId);

    @Transactional
    LoginResponse login(LoginRequest request);

    LoginResponse refreshToken(RefreshTokenRequest request);

    @Transactional
    void logout(Long userId);

    void sendOtp(String email);

    boolean verifyOtp(String email, String otp);

    void forgotPassword(String email);

    void resetPassword(ResetPasswordRequest request);

    void changePassword(Long userId, ChangePasswordRequest request);

    boolean validateToken(String token);

    void revokeToken(String token);

    void revokeAllTokens(Long userId);
}
