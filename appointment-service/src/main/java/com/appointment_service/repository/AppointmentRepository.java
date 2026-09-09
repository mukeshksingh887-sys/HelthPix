package com.appointment_service.repository;

import com.appointment_service.dto.AppointmentResponse;
import com.appointment_service.entity.Appointment;
import com.appointment_service.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {




    //  // 4. Check doctor slot availabe or not
    boolean existsByDoctorIdAndAppointmentDateAndStartTime(Long doctorId, LocalDate date, LocalTime time);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientId(Long patientId);

    Appointment findByAppointmentId(Long appointmentId);

    List<Appointment> findByDoctorIdAndAppointmentDate(
            Long doctorId,
            LocalDate appointmentDate);


    //chek for the slot and status
//    boolean existsByDoctorIdAndAppointmentDateAndStartTimeAndStatusNot(Long doctorId, LocalDate appointmentDate, LocalTime startTime, LocalTime endTime, AppointmentStatus status);
boolean existsByPatientIdAndAppointmentDateAndStartTimeAndEndTimeAndStatusNot(
        Long doctorId,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime,
        AppointmentStatus status);

    // chek for exit by patient and slot
//    Patient already has an appointment at this time or note
    boolean existsByDoctorIdAndAppointmentDateAndStartTimeAndEndTimeAndStatusNot(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime startTime,
            LocalTime endTime,
            AppointmentStatus status);




    boolean existsByPatientIdAndAppointmentDateAndStartTime(
            Long patientId,
            LocalDate appointmentDate,
            LocalTime startTime);

    boolean existsByDoctorIdAndAppointmentDateAndEndTime(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime endTime);
}