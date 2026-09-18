package com.auth_service.service;

import com.auth_service.dto.AuthUserResponse;
import com.auth_service.entity.UserType;

import java.util.List;

public interface AuthQueryService {

    AuthUserResponse getCurrentUser(Long userId);

    AuthUserResponse getByUserId(Long userId);

    AuthUserResponse getByEmail(String email);

    List<AuthUserResponse> getByRole(UserType role);

//    List<AuthUserResponse> getByStatus(UserStatus status);
}
