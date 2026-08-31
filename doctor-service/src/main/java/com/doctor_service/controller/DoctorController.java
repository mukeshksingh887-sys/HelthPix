package com.doctor_service.controller;

import com.doctor_service.dto.CreateDoctorRequest;
import com.doctor_service.dto.DoctorResponse;
import com.doctor_service.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    /**
     * Create Doctor
     */
    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @Valid @RequestBody CreateDoctorRequest request) {

        DoctorResponse response =
                doctorService.createDoctor(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Get Doctor By Doctor Id
     */
    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorById(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                doctorService.getDoctorById(doctorId)
        );
    }

    /**
     * Get Doctor By User Id
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<DoctorResponse> getDoctorByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                doctorService.getDoctorByUserId(userId)
        );
    }

    /**
     * Get All Doctors
     */
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {

        return ResponseEntity.ok(
                doctorService.getAllDoctors()
        );
    }

    /**
     * Update Doctor
     */
    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long doctorId,
            @Valid @RequestBody CreateDoctorRequest request) {

        return ResponseEntity.ok(
                doctorService.updateDoctor(
                        doctorId,
                        request
                )
        );
    }

    /**
     * Delete Doctor
     */
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<String> deleteDoctor(
            @PathVariable Long doctorId) {

        doctorService.deleteDoctor(doctorId);

        return ResponseEntity.ok(
                "Doctor deleted successfully"
        );
    }
}