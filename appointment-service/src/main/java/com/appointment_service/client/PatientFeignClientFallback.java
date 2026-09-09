package com.appointment_service.client;

import com.appointment_service.dto.PatientResponse;
import com.appointment_service.exception.ServiceUnavailableException;

public class PatientFeignClientFallback implements  PatientServiceClient{
    @Override
    public PatientResponse getPatientById(Long patientId) {
        throw new ServiceUnavailableException("PATIENT - SERVICE is unavailable");
    }

//    @Override
//    public PatientResponse getPatientByEmail(String email) {
//        throw new ServiceUnavailableException("PATIENT - SERVICE is unavailable");
//    }
}
