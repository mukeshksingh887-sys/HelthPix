package com.auth_service.service;
import com.auth_service.client.UserFeignClient;
import com.auth_service.dto.AuthUserResponse;
import com.auth_service.dto.UserResponse;
import com.auth_service.entity.AuthUser;
import com.auth_service.entity.UserType;
import com.auth_service.exception.ResourceNotFoundException;
import com.auth_service.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthQueryServiceImpl
        implements AuthQueryService {

    private final AuthUserRepository repository;
    private final UserFeignClient userFeignClient;

    @Override
    public AuthUserResponse getCurrentUser(Long userId) {

        return getByUserId(userId);
    }

    @Override
    public AuthUserResponse getByUserId(Long userId) {

        AuthUser authUser = repository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Auth user not found"));

        UserResponse user =
                userFeignClient.getUserById(userId);

        return buildResponse(authUser, user);
    }

    @Override
    public AuthUserResponse getByEmail(String email) {

        AuthUser authUser = repository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Auth user not found"));

        UserResponse user =
                userFeignClient.getUserByEmail(email);

        return buildResponse(authUser, user);
    }

    @Override
    public List<AuthUserResponse> getByRole(
            UserType role) {

        return repository.findByRole(role)
                .stream()
                .map(authUser -> {

                    UserResponse user =
                            userFeignClient.getUserById(
                                    authUser.getUserId());

                    return buildResponse(authUser, user);

                })
                .toList();
    }

//    @Override
//    public List<AuthUserResponse> getByStatus(
//            UserStatus status) {
//
//        return repository.findByStatus(status)
//                .stream()
//                .map(authUser -> {
//
//                    UserResponse user =
//                            userFeignClient.getUserById(
//                                    authUser.getUserId());
//
//                    return buildResponse(authUser, user);
//
//                })
//                .toList();
//    }

    private AuthUserResponse buildResponse(
            AuthUser authUser,
            UserResponse user) {

        return AuthUserResponse.builder()
                .userId(authUser.getUserId())
                .email(authUser.getEmail())
                .role(authUser.getRole())
                .build();
    }
}

