package com.ai_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder


@NoArgsConstructor
@AllArgsConstructor
public class MedicineSuggestionRequest {
//    @NotBlank
    String patientId;

//    @NotBlank
    String symptoms;

    Integer age;

    String gender;

    Double weightKg;

    List<String> allergies;

    List<String> currentMedications;

    List<String> existingConditions;

    String medicalHistory;

    String labReports;
}
