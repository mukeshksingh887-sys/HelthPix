package com.Patient_service.service;


import com.Patient_service.entity.AppointmentHistory;
import com.Patient_service.entity.Patient;
import com.Patient_service.repository.AppointmentHistoryRepository;
import com.Patient_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentHistoryService {


    @Autowired
    private    PatientRepository patientRepository;

    @Autowired
    private AppointmentHistoryRepository appointmentHistoryRepository;

    public AppointmentHistory createAppointment(
            Long patientId,
            AppointmentHistory appointment) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException("Patient not found"));

        appointment.setPatient(patient);

        return appointmentHistoryRepository.save(appointment);
    }
}
