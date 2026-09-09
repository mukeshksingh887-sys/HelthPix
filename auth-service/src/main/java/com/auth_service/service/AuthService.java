package com.auth_service.service;

import com.auth_service.client.UserFeignClient;
import com.auth_service.dto.*;
import com.auth_service.entity.AuthUser;
import com.auth_service.entity.RefreshToken;
import com.auth_service.exception.BadCredentialsException;
import com.auth_service.exception.UserAlreadyExistsException;
import com.auth_service.repository.AuthUserRepository;
import com.auth_service.repository.RefreshTokenRepository;
import com.auth_service.util.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    @Autowired
    private final AuthUserRepository authRepository;

    @Autowired
    private final RefreshTokenRepository refreshRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private  UserFeignClient userFeignClient;

    public void createAuthUser(AuthUserRequest request) {

//        if (authRepository.existsByEmail(request.getEmail())) {
//            throw new UserAlreadyExistsException(
//                    "Email already exists"
//            );
//        }

        AuthUser authUser = AuthUser.builder()
                .userId(request.getUserId())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        authRepository.save(authUser);
    }


    public void deleteAuthUser(Long userId) {

        AuthUser authUser = authRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        authRepository.delete(authUser);
    }


    @Transactional
    public LoginResponse login(LoginRequest request) {

        AuthUser authUser = authRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));


        log.info("password: {} ", request.getPassword());
        log.info(" DB password:{} " , authUser.getPassword());

        boolean matched = passwordEncoder.matches(request.getPassword(), authUser.getPassword());
        log.info("compare : {}", matched);


        if (!passwordEncoder.matches(
                request.getPassword(),
                authUser.getPassword())) {

             throw new BadCredentialsException("Invalid username or password");
        }

        // Fetch complete user data from User Service

        UserResponse user =
                userFeignClient.getByEmail(
                        authUser.getEmail());

        log.info("data from user_dB : {}",user.getEmail());

//        String accessToken =
//                jwtService.generateAccessToken(
//                        user.getUserId(),
//                        user.getEmail(),
//                        user.getRole().name());

        String accessToken = jwtService.generateAccessToken(String.valueOf(user.getId()), user.getEmail(),user.getRole());

        String refreshToken =
                jwtService.generateRefreshToken(String.valueOf(user.getId()), user.getEmail(),user.getRole());

        refreshRepository.deleteByUserId(
                user. getId());

        RefreshToken tokenEntity =
                RefreshToken.builder()
                        .userId(user. getId())
                        .token(refreshToken)
                        .expiryDate(
                                LocalDateTime.now()
                                        .plusDays(7))
                        .build();

        refreshRepository.save(tokenEntity);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }




    public LoginResponse refreshToken(
            RefreshTokenRequest request) {

        RefreshToken storedToken =
                refreshRepository.findByToken(
                                request.getRefreshToken())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid Refresh Token"));

        if (storedToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Refresh Token Expired");
        }

        AuthUser user =
                authRepository.findByUserId(
                                storedToken.getUserId())
                        .orElseThrow();

        String accessToken = jwtService.generateAccessToken(String.valueOf(user.getId()), user.getEmail(), String.valueOf(user.getRole()));

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(request.getRefreshToken())
                .userId(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}