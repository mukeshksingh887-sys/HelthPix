package com.doctor_service.client;

import org.springframework.cloud.openfeign.FeignClient;

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