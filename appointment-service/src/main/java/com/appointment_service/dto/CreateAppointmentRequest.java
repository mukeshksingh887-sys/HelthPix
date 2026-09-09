package com.appointment_service.dto;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequest {

    private Long patientId;

    private Long doctorId;

    private LocalDate appointmentDate;

//    private LocalTime appointmentTime;


    LocalTime startTime;


    LocalTime endTime;

    private String reason;

    private  String  notes;

}