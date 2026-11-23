package com.Patient_service.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDetailsResponse {

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

    private LocalDateTime updatedAt;

    private List<MedicalHistoryResponse> medicalHistories;

    private List<AppointmentHistoryResponse> appointmentHistory;
}
