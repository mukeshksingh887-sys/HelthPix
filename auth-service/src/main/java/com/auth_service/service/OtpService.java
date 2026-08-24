package com.auth_service.service;

import com.auth_service.entity.OtpVerification;
import com.auth_service.repository.OtpVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpVerificationRepository repository;

    public void generateOtp(String email) {

        String otp = String.format(
                "%06d",
                new SecureRandom().nextInt(1_000_000)
        );

        OtpVerification entity =
                new OtpVerification();

        entity.setEmail(email);
        entity.setOtp(otp);
        entity.setVerified(false);
        entity.setCreatedAt(LocalDateTime.now());

        entity.setExpiresAt(
                LocalDateTime.now()
                        .plusMinutes(5)
        );

        repository.save(entity);

        // Send OTP through Email/SMS provider.
        System.out.println(
                "OTP for " + email + " = " + otp
        );
    }

    public boolean verifyOtp(
            String email,
            String otp) {

        OtpVerification entity =
                repository
                        .findTopByEmailOrderByCreatedAtDesc(
                                email
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "OTP not found"
                                )
                        );

        if (entity.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            return false;
        }

        if (!entity.getOtp().equals(otp)) {
            return false;
        }

        entity.setVerified(true);

        repository.save(entity);

        return true;
    }
}