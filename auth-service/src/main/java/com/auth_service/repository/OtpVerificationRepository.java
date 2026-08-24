package com.auth_service.repository;

import com.auth_service.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, Long> {


    Optional<OtpVerification>
    findTopByEmailOrderByCreatedAtDesc(String email);
}
