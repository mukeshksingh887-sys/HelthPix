package com.auth_service.service;

import com.auth_service.client.UserFeignClient;
import com.auth_service.dto.*;
import com.auth_service.entity.AuthUser;
import com.auth_service.entity.OtpVerification;
import com.auth_service.entity.RefreshToken;
import com.auth_service.exception.BadCredentialsException;
import com.auth_service.exception.ResourceNotFoundException;
import com.auth_service.repository.AuthUserRepository;
import com.auth_service.repository.OtpVerificationRepository;
import com.auth_service.repository.RefreshTokenRepository;
import com.auth_service.util.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImp implements  AuthService {

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

    @Autowired
    private  OtpVerificationRepository otpVerificationRepository;


    @Override
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

    @Override
    public void deleteAuthUser(Long userId) {

        AuthUser authUser = authRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        authRepository.delete(authUser);
    }

    @Transactional
    @Override
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
                userFeignClient.getUserByEmail(
                        authUser.getEmail());

        log.info("data from user_dB : {}",user.getEmail());


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



    @Override
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

    @Transactional
    @Override
    public void logout(Long userId) {

        AuthUser authUser = authRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        RefreshToken refreshToken =
                refreshRepository
                        .findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Refresh token not found"));

        refreshRepository.delete(refreshToken);

        log.info("Logout successful for user : {}", userId);
    }



    @Override
    public void sendOtp(String email) {

        String otp = String.valueOf(
                100000 + new Random().nextInt(900000));

        otpVerificationRepository.findByEmail(email)
                .ifPresent(otpVerificationRepository::delete);

        OtpVerification verification =
                OtpVerification.builder()
                        .email(email)
                        .otp(otp)
                        .verified(false)
                        .expiryTime(
                                LocalDateTime.now().plusMinutes(5))
                        .build();

        otpVerificationRepository.save(verification);

        System.out.println("OTP : " + otp);

        // Send Email Here
    }



    @Override
    public boolean verifyOtp(
            String email,
            String otp) {

        OtpVerification verification =
                otpVerificationRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("OTP not found"));

        if (verification.getExpiryTime()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException("OTP expired");
        }

        boolean verified =
                verification.getOtp().equals(otp);

        if (verified) {
            verification.setVerified(true);
            otpVerificationRepository.save(verification);
        }

        return verified;
    }


    @Override
    public void forgotPassword(String email) {

        AuthUser user = authRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        sendOtp(user.getEmail());
    }
    @Override
    public void resetPassword(
            ResetPasswordRequest request) {

        if (!verifyOtp(
                request.getEmail(),
                request.getOtp())) {

            throw new BadCredentialsException("Invalid OTP");
        }

        AuthUser user = authRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()));

        authRepository.save(user);
    }

    @Override
    public void changePassword(
            Long userId,
            ChangePasswordRequest request) {

        AuthUser user = authRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {

            throw new BadCredentialsException(
                    "Old password incorrect");
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()));

        authRepository.save(user);
    }


    @Override
    public boolean validateToken(String token) {

        return jwtService.validateToken(token);
    }

    @Override
    public void revokeToken(String token) {

        refreshRepository.findByToken(token)
                .ifPresent(refreshRepository::delete);
    }

    @Override
    public void revokeAllTokens(Long userId) {

        refreshRepository.deleteByUserId(userId);
    }

}