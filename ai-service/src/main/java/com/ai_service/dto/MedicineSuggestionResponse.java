package com.ai_service.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineSuggestionResponse {

    private String patientId;

    private String assessment;

    private List<MedicineSuggestion> suggestions;

    private List<String> warnings;

    private List<String> missingInformation;

    private boolean doctorReviewRequired;
}
