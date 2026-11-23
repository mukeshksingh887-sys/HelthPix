package com.Patient_service.dto;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentHistoryResponse {

    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private String doctorName;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentStatus status;
    private String reason;
    private String notes;

    private PatientResponse patient;

    private DoctorResponse doctor;
}
