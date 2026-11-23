package com.Patient_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class MedicalHistoryRequest {


    private String doctorName;

    private String disease;

    private String allergies;

    private String medications;

    private String surgeryDetails;

    private String notes;

    private String doctorNotes;

    private LocalDate diagnosisDate;
}