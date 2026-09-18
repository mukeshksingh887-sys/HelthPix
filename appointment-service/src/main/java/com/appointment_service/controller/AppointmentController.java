package com.appointment_service.controller;

import com.appointment_service.dto.AppointmentResponse;
import com.appointment_service.dto.CreateAppointmentRequest;
import com.appointment_service.dto.RescheduleRequest;
import com.appointment_service.service.AppointmentService;
import com.appointment_service.service.AppointmentServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    @Autowired
    private AppointmentServiceImp appointmentServiceImp;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<AppointmentResponse> create(@RequestBody CreateAppointmentRequest request) {
//        AppointmentResponse appointmentResponse = appointmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentServiceImp.createAppointment(request));
    }


    @GetMapping("get/{id}")
    public ResponseEntity<AppointmentResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentById(id));
    }




    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>>
    getByPatient(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                appointmentService.getByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>>
    getByDoctor(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                appointmentService.getByDoctorId(doctorId));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse>
    cancelAppointment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.cancelAppointment(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<AppointmentResponse>
    completeAppointment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.completeAppointment(id));
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentResponse>
    rescheduleAppointment(
            @PathVariable Long id,
            @RequestBody RescheduleRequest request) {

        return ResponseEntity.ok(
                appointmentService.rescheduleAppointment(id,
                        request));
    }

    @GetMapping("/available-slots/{doctorId}")
    public ResponseEntity<List<LocalTime>>
    availableSlots(
            @PathVariable Long doctorId,
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                appointmentService.getAvailableSlots(
                        doctorId,
                        date));
    }


}