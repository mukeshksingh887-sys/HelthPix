package com.Patient_service.util;

import com.Patient_service.dto.PatientResponse;
import com.Patient_service.entity.Patient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class Util {

    public PatientResponse mapToResponse(Patient patient) {

        PatientResponse response = new PatientResponse();

        response.setId(patient.getId());
        response.setPatientCode(patient.getPatientCode());
        response.setFirstName(patient.getFirstName());
        response.setLastName(patient.getLastName());
        response.setEmail(patient.getEmail());
        response.setPhone(patient.getPhone());
        response.setDateOfBirth(patient.getDateOfBirth());
        response.setGender(String.valueOf(patient.getGender()));
        response.setBloodGroup(patient.getBloodGroup());
        response.setAddress(patient.getAddress());
        response.setEmergencyContact(patient.getEmergencyContact());
        response.setCreatedAt(patient.getCreatedAt());
        response.setStatus(String.valueOf(patient.getStatus()));
        response.setUpdatedAt(LocalDateTime.now());

        return response;
    }

    private String generatePatientCode() {

        return "PAT-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8);

    }

}
