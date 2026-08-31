package com.doctor_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Data
public class CreateDoctorRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String specialization;

    @NotBlank
    private String qualification;

    @Min(0)
    private Integer experienceYears;

    @DecimalMin("0.0")
    private BigDecimal consultationFee;

    @NotBlank
    private String department;

    @NotBlank
    private String licenseNumber;
}
