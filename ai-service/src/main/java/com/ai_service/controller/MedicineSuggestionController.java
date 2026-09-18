package com.ai_service.controller;




//import jakarta.validation.Valid;

import com.ai_service.dto.MedicineSuggestionRequest;
import com.ai_service.dto.MedicineSuggestionResponse;
import com.ai_service.service.MedicineSuggestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class MedicineSuggestionController {

    private final MedicineSuggestionService medicineSuggestionService;

    public MedicineSuggestionController(
            MedicineSuggestionService medicineSuggestionService) {

        this.medicineSuggestionService =
                medicineSuggestionService;
    }

    @PostMapping("/medicine-suggestion")
    public ResponseEntity<MedicineSuggestionResponse>
    generateMedicineSuggestion(

//            @Valid
            @RequestBody
            MedicineSuggestionRequest request) {

        MedicineSuggestionResponse response =
                medicineSuggestionService
                        .generateSuggestion(request);

        return ResponseEntity.ok(response);
    }


//    @PreAuthorize("hasRole('DOCTOR')")
//    @PostMapping("/medicine-suggestion")
//    public ResponseEntity<MedicineSuggestionResponse>
//    generateMedicineSuggestion(
//            @Valid @RequestBody MedicineSuggestionRequest request) {
//
//        return ResponseEntity.ok(
//                medicineSuggestionService
//                        .generateSuggestion(request)
//        );
//    }
}