package com.Patient_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Patient Reference
    @Column(nullable = false)
    private Long patientId;

    // Disease / Diagnosis
    @Column(nullable = false)
    private String diagnosis;

    // Symptoms
    @Column(length = 1000)
    private String symptoms;

    // Treatment Details
    @Column(length = 2000)
    private String treatment;

    // Prescribed Medicines
    @Column(length = 2000)
    private String medications;

    // Doctor Information
    private Long doctorId;

    private String doctorName;

    // Dates
    private LocalDate diagnosisDate;

    private LocalDate recoveryDate;

    // Additional Notes
    @Column(length = 2000)
    private String notes;

//    @Enumerated(EnumType.STRING)
//    private MedicalHistoryStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}