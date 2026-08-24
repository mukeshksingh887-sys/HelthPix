package com.auth_service.service;

import com.auth_service.dto.AuthResponse;
import com.auth_service.dto.LoginRequest;

public interface AuthService {

    public AuthResponse login(
            LoginRequest request,
            String ipAddress);
}
