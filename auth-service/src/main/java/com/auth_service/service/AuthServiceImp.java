package com.auth_service.service;


import com.auth_service.dto.AuthResponse;
import com.auth_service.dto.LoginRequest;
import com.auth_service.dto.RegisterRequest;
import com.auth_service.entity.*;
import com.auth_service.exception.BadCredentialsException;
import com.auth_service.repository.*;
import com.auth_service.util.JwtServiceImp;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
//@RequiredArgsConstructor
@Transactional
public class AuthServiceImp  {


    private final UserAccountRepository userRepository;

    private final RoleRepository roleRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final LoginAttemptRepository loginAttemptRepository;

    private final AuditLogRepository auditLogRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtServiceImp jwtService;

    public AuthServiceImp(UserAccountRepository userRepository, RoleRepository roleRepository, RefreshTokenRepository refreshTokenRepository, LoginAttemptRepository loginAttemptRepository, AuditLogRepository auditLogRepository, PasswordEncoder passwordEncoder, JwtServiceImp jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.loginAttemptRepository = loginAttemptRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Value("${auth.max-login-attempts}")
    private int maxLoginAttempts;

    @Value("${auth.lock-duration-minutes}")
    private int lockDurationMinutes;





    public AuthResponse login(
            LoginRequest request
          ) {

        UserAccount user =
                userRepository.findByUsername(
                        request.getUsername()
                ).orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid username or password"
                        )
                );

        if (user.isAccountLocked()) {

            if (user.getLockedUntil() != null &&
                    user.getLockedUntil()
                            .isBefore(LocalDateTime.now())) {

                user.setAccountLocked(false);
                user.setFailedLoginAttempts(0);

            } else {

                throw new RuntimeException(
                        "Account temporarily locked"
                );
            }
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        user.setFailedLoginAttempts(0);

        String accessToken =
                jwtService.generateAccessToken(user);

        String refreshToken =
                jwtService.generateRefreshToken(user);


        RefreshToken token = new RefreshToken();

        token.setToken(refreshToken);
        token.setUser(user);
        token.setRevoked(false);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(
                LocalDateTime.now().plusDays(7)
        );

        refreshTokenRepository.save(token);





        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                900
        );
    }

    private void handleFailedLogin(
            UserAccount user,
            String ipAddress) {

        int attempts =
                user.getFailedLoginAttempts() + 1;

        user.setFailedLoginAttempts(attempts);

        if (attempts >= maxLoginAttempts) {

            user.setAccountLocked(true);

            user.setLockedUntil(
                    LocalDateTime.now()
                            .plusMinutes(lockDurationMinutes)
            );
        }

        saveLoginAttempt(
                user.getUsername(),
                ipAddress,
                false
        );

        saveAudit(
                user.getUsername(),
                "LOGIN_FAILED",
                ipAddress,
                false
        );
    }

    private void saveLoginAttempt(
            String username,
            String ip,
            boolean successful) {

        LoginAttempt attempt =
                new LoginAttempt();

        attempt.setUsername(username);
        attempt.setIpAddress(ip);
        attempt.setSuccessful(successful);
        attempt.setAttemptedAt(
                LocalDateTime.now()
        );

        loginAttemptRepository.save(attempt);
    }

    private void saveAudit(
            String username,
            String action,
            String ip,
            boolean successful) {

        AuditLog audit = new AuditLog();

        audit.setUsername(username);
        audit.setAction(action);
        audit.setIpAddress(ip);
        audit.setSuccessful(successful);
        audit.setCreatedAt(
                LocalDateTime.now()
        );

        auditLogRepository.save(audit);
    }


    // register user
    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(
                request.getUsername())) {

            throw new IllegalArgumentException(
                    "Username already exists"
            );
        }

        if (userRepository.existsByEmail(
                request.getEmail() )) {

            throw new IllegalArgumentException(
                    "Email already exists"
            );
        }

        Role role = roleRepository
                .findByName(request.getRole())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Role not found"
                        )
                );

        UserAccount user = new UserAccount();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail() );

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setEnabled(true);
        user.setAccountLocked(false);

        user.getRoles().add(role);

        userRepository.save(user);
    }

    // refresh token

    public AuthResponse refreshToken(
            String refreshToken) {

        RefreshToken stored =
                refreshTokenRepository
                        .findByToken(refreshToken)
                        .orElseThrow(() ->
                                new BadCredentialsException(
                                        "Invalid refresh token"
                                )
                        );

        if (stored.isRevoked()) {
            throw new BadCredentialsException(
                    "Refresh token revoked"
            );
        }

        if (stored.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            stored.setRevoked(true);

            throw new BadCredentialsException(
                    "Refresh token expired"
            );
        }

        UserAccount user =
                stored.getUser();

        String newAccessToken =
                jwtService.generateAccessToken(user);

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                "Bearer",
                900
        );
    }



    // logout
    public void logout(String refreshToken) {

        RefreshToken token =
                refreshTokenRepository
                        .findByToken(refreshToken)
                        .orElseThrow(() ->
                                new BadCredentialsException(
                                        "Invalid token"
                                )
                        );

        token.setRevoked(true);

        refreshTokenRepository.save(token);
    }
}

