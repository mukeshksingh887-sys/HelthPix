package com.Patient_service.dto;

import com.Patient_service.entity.Patient;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppointmentHistoryRequest {
    private  String doctorId;
    private LocalDate appointmentDate;
    private String doctorName;
    private String reason;
    private String status;
}


