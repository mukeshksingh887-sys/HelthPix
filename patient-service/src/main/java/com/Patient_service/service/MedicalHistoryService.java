package com.Patient_service.service;

import com.Patient_service.entity.MedicalHistory;
import com.Patient_service.entity.Patient;
import com.Patient_service.repository.MedicalHistoryRepository;
import com.Patient_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalHistoryService {

    private final PatientRepository patientRepository;
    private final MedicalHistoryRepository historyRepository;

    public MedicalHistory createHistory(
            Long patientId,
            MedicalHistory history) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException("Patient not found"));

        history.setPatient(patient);

        return historyRepository.save(history);
    }
}