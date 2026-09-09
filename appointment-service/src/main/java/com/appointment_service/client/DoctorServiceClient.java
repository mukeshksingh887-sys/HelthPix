package com.appointment_service.client;

import com.appointment_service.dto.DoctorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;





@FeignClient(name = "DOCTOR-SERVICE",  fallback = DoctorFeignClientFallback.class)
    public interface DoctorServiceClient {

//     @GetMapping("/internal/doctors/{doctorEmail}")
//     public DoctorResponse  getDoctorByEmail(@PathVariable String email);


    @GetMapping("/internal/doctors/{doctorId}")
    public DoctorResponse getDoctorById(@PathVariable("doctorId") Long doctorId);
}



