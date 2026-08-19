package com.Patient_service.service;

import com.Patient_service.dto.PatientRequest;
import com.Patient_service.dto.PatientResponse;
import com.Patient_service.util.PatientStatus;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PatientService {

    PatientResponse createPatient(PatientRequest request);

    PatientResponse getPatientById(Long id);

    List<PatientResponse> getAllPatients();

    PatientResponse updatePatient(
            Long id,
            PatientRequest request
    );

    PatientResponse PartialPatientUpdate(Long id, PatientRequest request);

    PatientResponse deletePatient(Long id);

    PatientResponse updateStatus(
            Long id,
            PatientStatus status
    );

    List<PatientResponse> searchPatients(String keyword);


        List<PatientResponse> getPatientsByStatus(String status);



}
