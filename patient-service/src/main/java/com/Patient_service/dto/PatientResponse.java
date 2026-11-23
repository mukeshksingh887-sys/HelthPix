package com.Patient_service.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {

    private Long patientId;
    private String patientCode;
    private Long userId;

    // Data fetched from User Service
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private List<MedicalHistoryResponse> medicalHistories;
    private List<AppointmentHistoryResponse> appointmentHistory;
}