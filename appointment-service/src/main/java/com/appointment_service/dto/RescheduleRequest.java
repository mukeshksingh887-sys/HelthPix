package com.appointment_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RescheduleRequest {

    private LocalDate appointmentDate;

    private LocalTime startTime;
}