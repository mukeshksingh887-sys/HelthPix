package com.Patient_service.service;

import com.Patient_service.dto.PatientRequest;
import com.Patient_service.dto.PatientResponse;
import com.Patient_service.entity.PatientStatus;

import java.util.List;


public interface PatientService {

    public PatientResponse createPatient(PatientRequest request);

    public PatientResponse getPatientById(Long id);

    public List<PatientResponse> getAllPatients();

    public PatientResponse updatePatient(Long id, PatientRequest request);

    public PatientResponse PartialPatientUpdate(Long id, PatientRequest request);

    public PatientResponse deletePatient(Long id);

    public PatientResponse updateStatus(Long id, PatientStatus status);

    public List<PatientResponse> searchPatients(String keyword);


    public List<PatientResponse> getPatientsByStatus(String status);


    public PatientResponse getPatientByEmail(String email);

    List<PatientResponse> getPatientsByBloodGroup(String bloodGroup);

    long countPatients();


//        public MedicineSuggestionResponse getMedicineSuggestion(MedicineSuggestionRequest request);
}
