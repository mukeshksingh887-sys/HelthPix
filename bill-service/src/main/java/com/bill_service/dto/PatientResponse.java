package com.bill_service.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {

    private Long id;
    private String patientCode;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
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
