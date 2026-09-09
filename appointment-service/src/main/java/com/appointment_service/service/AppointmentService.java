package com.appointment_service.service;

import com.appointment_service.dto.AppointmentResponse;
import com.appointment_service.dto.CreateAppointmentRequest;
import com.appointment_service.dto.RescheduleRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {


   public AppointmentResponse createAppointment(CreateAppointmentRequest request);

    public AppointmentResponse getAppointmentById(Long id);

    List<AppointmentResponse> getByPatientId(Long patientId);

    List<AppointmentResponse> getByDoctorId(Long doctorId);

    AppointmentResponse cancelAppointment(Long id);

    AppointmentResponse completeAppointment(Long id);

    AppointmentResponse rescheduleAppointment(Long id, RescheduleRequest request);

    List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date);

}
