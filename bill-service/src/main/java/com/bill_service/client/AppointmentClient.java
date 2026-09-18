package com.bill_service.client;

import com.bill_service.dto.AppointmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "APPOINTMENT-SERVICE")
public interface AppointmentClient {

    @GetMapping("/api/appointments/{id}")
    AppointmentResponse getAppointment(
            @PathVariable Long id);
}