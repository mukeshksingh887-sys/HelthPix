package com.Patient_service.event;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientEvent {

    private String eventType;

    private Long patientId;

    private String patientName;

    private String email;

    private String phone;

    private LocalDateTime timestamp;
}
