package com.Patient_service.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistoryResponse {

    private Long id;

    private String doctorName;

    private String disease;

    private String allergies;

    private String medications;

    private String surgeryDetails;

    private String diagnosis;

    private LocalDate diagnosisDate;

    private String doctorNotes;

    private  String notes;
}