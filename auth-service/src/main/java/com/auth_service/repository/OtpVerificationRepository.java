package com.auth_service.repository;

import com.auth_service.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findByEmail(String email);

    Optional<OtpVerification> findByEmailAndOtp(
            String email,
            String otp);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}