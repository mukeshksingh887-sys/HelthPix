package com.Patient_service.dto;

import lombok.*;
import org.hibernate.annotations.processing.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PatientRequest {

    private String firstName;
    private String lastName;
    private String email;
    private Long userId;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String address;
    private String emergencyContact;
    private String status;
    private LocalDateTime createdAt;

    private  LocalDateTime updatedAt;
}
