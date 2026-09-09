package com.appointment_service.client;


import com.appointment_service.dto.PatientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "PATIENT-SERVICE",  fallback = DoctorFeignClientFallback.class)
public interface PatientServiceClient {
    @GetMapping("/internal/patients/{patientId}")
    public PatientResponse getPatientById(
            @PathVariable("patientId") Long patientId);

//
//    @GetMapping("/internal/patients/{email}")
//    public  PatientResponse getPatientByEmail(@PathVariable String email);

}
