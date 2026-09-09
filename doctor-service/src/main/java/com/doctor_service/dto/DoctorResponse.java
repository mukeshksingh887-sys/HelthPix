package com.doctor_service.dto;

import com.doctor_service.enitiy.DoctorStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private Long doctorId;
    private Long userId;
    private String specialization;
    private String qualification;
    private Integer experienceYears;
    private BigDecimal consultationFee;
    private String department;
    private String licenseNumber;
    private DoctorStatus status;

    private UserResponse user;
}