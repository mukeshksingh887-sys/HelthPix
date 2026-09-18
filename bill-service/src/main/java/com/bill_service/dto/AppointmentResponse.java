package com.bill_service.dto;


//import com.appointment_service.entity.AppointmentStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentResponse {

    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
//    private AppointmentStatus status;
    private String reason;
    private String notes;

    private PatientResponse patient;

    private DoctorResponse doctor;
}
