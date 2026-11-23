package com.Patient_service.dto;

import com.Patient_service.entity.AppointmentHistory;
import lombok.*;
import org.hibernate.annotations.processing.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PatientRequest {
    private Long userId;

    private List<MedicalHistoryRequest> medicalHistories;

    private  List<AppointmentHistory>  appointmentHistories;
}
