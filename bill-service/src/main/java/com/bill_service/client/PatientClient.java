package com.bill_service.client;

import com.bill_service.dto.PatientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PATIENT-SERVICE")
public interface PatientClient {

    @GetMapping("/api/patients/{id}")
    PatientResponse getPatient(
            @PathVariable Long id);
}