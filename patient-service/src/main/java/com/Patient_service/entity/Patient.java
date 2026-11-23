package com.Patient_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @Column(unique = true, nullable = false)
    private String patientCode;

    @Column(nullable = false)
    private Long userId;

    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<MedicalHistory> medicalHistory = new ArrayList<>();

    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<AppointmentHistory> appointmentHistory = new ArrayList<>();

    private LocalDateTime createdAt;

    private  LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public void addMedicalHistory(MedicalHistory history) {

        if (medicalHistory == null) {
            medicalHistory = new ArrayList<>();
        }

        medicalHistory.add(history);

        history.setPatient(this);
    }

    public void addAppointmentHistory(
            AppointmentHistory appointment) {


        if (appointmentHistory == null) {
            appointmentHistory = new ArrayList<>();
        }

        appointmentHistory.add(appointment);

        appointment.setPatient(this);
    }

}
