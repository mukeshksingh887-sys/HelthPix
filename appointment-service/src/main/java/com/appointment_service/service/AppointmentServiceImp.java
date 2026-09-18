package com.appointment_service.service;

import com.appointment_service.client.DoctorServiceClient;
import com.appointment_service.client.PatientServiceClient;
import com.appointment_service.dto.*;
import com.appointment_service.entity.Appointment;
import com.appointment_service.entity.AppointmentStatus;
import com.appointment_service.exception.AppointmentNotFoundException;
import com.appointment_service.exception.DocterNotFoundException;
import com.appointment_service.exception.PatientNotFoundException;

import com.appointment_service.exception.SlotNotAvailableException;
import com.appointment_service.repository.AppointmentRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
//@Builder
public class AppointmentServiceImp implements AppointmentService {

    @Autowired
    public AppointmentRepository appointmentRepository;
    @Autowired
    public DoctorServiceClient doctorServiceClient;
    @Autowired
    public PatientServiceClient patientServiceClient;
    


    /// 2nd create appointment

//    @Transactional
    public AppointmentResponse createAppointment(
            CreateAppointmentRequest request
    ) {

        PatientResponse patient = patientServiceClient.getPatientById(request.getPatientId());
        log.info(" patient info  {}" , patient);

        if (patient == null) {
            throw new PatientNotFoundException("Patient not found");
        }

        // 2. Validate doctor
        DoctorResponse doctor = doctorServiceClient.getDoctorById(request.getDoctorId());
        log.info(" doctor info:  {}", doctor);


        if (doctor == null || !doctor.getUser().getStatus().equals("ACTIVE") ) {
            throw new DocterNotFoundException("Doctor not found or inactive");
        }

        // 3. Validate time
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // validate date
        if (request.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Past date appointment not allowed");
        }

        // this also validate time
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new RuntimeException("End time must be after start time");
        }

       // check for doctor available
        if (isOverlapping(
                request.getDoctorId(),
                request.getAppointmentDate(),
                request.getStartTime(),
                request.getEndTime())) {

            throw new SlotNotAvailableException("Doctor is not available at this time");
        }

        // 4. Check doctor slot
        boolean doctorBusy =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndStartTimeAndEndTimeAndStatusNot(
                                request.getDoctorId(),
                                request.getAppointmentDate(),
                                request.getStartTime(),
                                request.getEndTime(),
                                AppointmentStatus.CANCELLED
                        );


        if (doctorBusy) {
            throw new SlotNotAvailableException("Doctor is not available at this time");
        }

        // 5. Check patient slot
        boolean patientBusy =
                appointmentRepository
                        .existsByPatientIdAndAppointmentDateAndStartTimeAndEndTimeAndStatusNot(
                                request.getPatientId(),
                                request.getAppointmentDate(),
                                request.getStartTime(),
                                request.getEndTime(),
                                AppointmentStatus.CANCELLED
                        );

        if (patientBusy) {
            throw new SlotNotAvailableException("Patient already has an appointment at this time");
        }


        // 6. Create appointment
        Appointment appointment = Appointment.builder()
                        .patientId(request.getPatientId())
                        .doctorId(request.getDoctorId())
                        .appointmentDate(request.getAppointmentDate())
                        .startTime(request.getStartTime())
                        .endTime(request.getEndTime())
                        .reason(request.getReason())
                        .notes(request.getNotes())
                        .status(AppointmentStatus.CONFIRMED)
                        .build();

        Appointment saved = appointmentRepository.save(appointment);


        return mapToResponse(
                saved,
                patient,
                doctor
        );
    }

    private boolean isOverlapping(
            Long doctorId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime) {

        List<Appointment> appointments =
                appointmentRepository.findByDoctorIdAndAppointmentDate(
                        doctorId,
                        date);

        for (Appointment appointment : appointments) {

            if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
                continue;
            }

            boolean overlap = startTime.isBefore(appointment.getEndTime()) && endTime.isAfter(appointment.getStartTime());

            if (overlap) {
                return true;
            }
        }

        return false;
    }










    @Override
    public AppointmentResponse getAppointmentById(
            Long id)  {
         Appointment appointment = appointmentRepository.findByAppointmentId(id);

      if (appointment == null){
          throw  new RuntimeException("appointment not found exception");
      }


        PatientResponse patient =
                patientServiceClient.getPatientById(appointment.getPatientId());

        DoctorResponse doctor = doctorServiceClient.getDoctorById(appointment.getDoctorId());

        return mapToResponse(
                appointment,
                patient,
                doctor
        );
    }

    @Override
    public List<AppointmentResponse> getByPatientId(Long patientId) {
        List<Appointment> appointments =
                appointmentRepository.findByPatientId(patientId);

        List<AppointmentResponse> responses =
                new ArrayList<>();

        for (Appointment appointment : appointments) {

            PatientResponse patient =
                    patientServiceClient.getPatientById(
                            appointment.getPatientId());

            DoctorResponse doctor =
                    doctorServiceClient.getDoctorById(
                            appointment.getDoctorId());

            AppointmentResponse response =
                    map(
                            appointment,
                            patient,
                            doctor);

            responses.add(response);
        }

        return responses;
    }

    @Override
    public List<AppointmentResponse> getByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .map(a -> {

                    PatientResponse patient =
                            patientServiceClient.getPatientById(
                                    a.getPatientId());

                    DoctorResponse doctor =
                            doctorServiceClient.getDoctorById(
                                    a.getDoctorId());

                    return map(
                            a,
                            patient,
                            doctor);

                }).toList();
    }



    @Override
    public AppointmentResponse cancelAppointment(
            Long id) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new AppointmentNotFoundException(
                                        "Appointment not found"));

        if (appointment.getStatus() ==
                AppointmentStatus.COMPLETED) {

            throw new AppointmentNotFoundException(
                    "Completed appointment cannot be cancelled");
        }

        appointment.setStatus(
                AppointmentStatus.CANCELLED);

        appointment.setUpdatedAt(
                LocalDateTime.now());

        appointment =appointmentRepository.save(appointment);

        return getAppointmentById(
                appointment.getAppointmentId());
    }

    @Override
    public AppointmentResponse completeAppointment(
            Long id) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new AppointmentNotFoundException(
                                        "Appointment not found"));

        if (appointment.getStatus() !=
                AppointmentStatus.SCHEDULED) {

            throw new RuntimeException(
                    "Only scheduled appointment can be completed");
        }

        appointment.setStatus(
                AppointmentStatus.COMPLETED);

        appointment.setUpdatedAt(
                LocalDateTime.now());

        appointment = appointmentRepository.save(appointment);

        return getAppointmentById(
                appointment.getAppointmentId());
    }



    @Override
    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {

        // Doctor working slots
        List<LocalTime> allSlots = Arrays.asList(
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
                LocalTime.of(14, 0),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0)
        );

        List<Appointment> appointments =
                appointmentRepository.findByDoctorIdAndAppointmentDate(
                        doctorId,
                        date);

        List<LocalTime> bookedSlots = new ArrayList<>();

        for (Appointment appointment : appointments) {

            if (appointment.getStatus() != AppointmentStatus.CANCELLED) {
                bookedSlots.add(
                        appointment.getStartTime());
            }
        }

        List<LocalTime> availableSlots = new ArrayList<>();

        for (LocalTime slot : allSlots) {

            if (!bookedSlots.contains(slot)) {
                availableSlots.add(slot);
            }
        }

        return availableSlots;
    }


    @Override
    public AppointmentResponse rescheduleAppointment(
            Long id,
            RescheduleRequest request) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found"));

        boolean booked =
              appointmentRepository.existsByDoctorIdAndAppointmentDateAndStartTime(
                        appointment.getDoctorId(),
                        request.getAppointmentDate(),
                        request.getStartTime());

        if (booked) {
            throw new RuntimeException(
                    "Slot already booked");
        }

        appointment.setAppointmentDate(
                request.getAppointmentDate());

        appointment.setStartTime(
                request.getStartTime());

        appointment.setStatus(
                AppointmentStatus.RESCHEDULED);

        appointment.setUpdatedAt(
                LocalDateTime.now());

        appointmentRepository.save(appointment);

        return getAppointmentById(
                appointment.getAppointmentId());
    }


    private AppointmentResponse map(
            Appointment appointment,
            PatientResponse patient,
            DoctorResponse doctor) {

        return AppointmentResponse.builder()
                .appointmentId(appointment.getAppointmentId())
                .patientId(patient.getId())
//                .patientName(
//                        patient.getFirstName()
//                                + " "
//                                + patient.getLastName())
                .doctorId(doctor.getDoctorId())
//                .doctorName(
//                        doctor.getUser().getFirstName()
//                                + " "
//                                + doctor.getUser().getLastName())
//                .specialization(
//                        doctor.getSpecialization())
                .appointmentDate(
                        appointment.getAppointmentDate())
//                .appointmentTime(
//                        appointment.getAppointmentTime())
//                .reason(
//                        appointment.getReason())
                .status(
                        appointment.getStatus())
                .build();
    }



//    private AppointmentResponse mapToResponse(Appointment appointment) {
//
//        return new AppointmentResponse(
//                appointment.getAppointmentId(),
//                appointment.getPatientId(),
//                appointment.getDoctorId(),
//                appointment.getAppointmentDate(),
//                appointment.getStartTime(),
//                appointment.getEndTime(),
//                appointment.getStatus(),
//                appointment.getReason(),
//                appointment.getNotes());
//    }

    private AppointmentResponse mapToResponse(
            Appointment appointment,
            PatientResponse patient,
            DoctorResponse doctor) {

        return AppointmentResponse.builder()
                .appointmentId(appointment.getAppointmentId())
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(appointment.getAppointmentDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .status(appointment.getStatus())
                .reason(appointment.getReason())
                .notes(appointment.getNotes())
                .build();
    }

}
