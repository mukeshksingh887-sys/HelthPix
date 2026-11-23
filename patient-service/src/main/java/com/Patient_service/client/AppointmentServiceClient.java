package com.Patient_service.client;

import com.Patient_service.dto.AppointmentHistoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "APPOINTMENT-SERVICE"
)
public interface AppointmentServiceClient {

    @GetMapping(
            "/internal/appointments/patient/{patientId}"
    )
    List<AppointmentHistoryResponse> getAppointmentsByPatientId(
            @PathVariable Long patientId
    );
}