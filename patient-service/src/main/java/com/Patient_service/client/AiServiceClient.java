package com.Patient_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "AI-SERVICE"

)
public interface AiServiceClient {

//    @PostMapping("/medicine-suggestion")
//    MedicineSuggestionResponse
//    generateMedicineSuggestion(
//            @RequestBody MedicineSuggestionRequest request
//    );
}