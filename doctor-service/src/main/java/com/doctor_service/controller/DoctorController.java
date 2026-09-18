package com.doctor_service.controller;

import com.doctor_service.dto.CreateDoctorRequest;
import com.doctor_service.dto.DoctorResponse;
import com.doctor_service.enitiy.DoctorStatus;
import com.doctor_service.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * Create Doctor
     */
    @PostMapping("/create")
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
    @GetMapping("get/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorById(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                doctorService.getDoctorById(doctorId)
        );
    }

    /**
     * Get Doctor By User Id
     */
    @GetMapping("/userbyuser/{userId}")
    public ResponseEntity<DoctorResponse> getDoctorByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                doctorService.getDoctorByUserId(userId)
        );
    }

    /**
     * Get All Doctors
     */
    @GetMapping("/getAllDoctor")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {

        return ResponseEntity.ok(
                doctorService.getAllDoctors()
        );
    }

    /**
     * Update Doctor
     */
//    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping("update/{doctorId}")
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
     * Delete Doctor by doctorId
     */
//    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @DeleteMapping("delete/{doctorId}")
    public ResponseEntity<String> deleteDoctor(
            @PathVariable Long doctorId) {

        doctorService.deleteDoctor(doctorId);

        return ResponseEntity.ok(
                "Doctor deleted successfully"
        );
    }



    @PutMapping("/{doctorId}/status")
    public ResponseEntity<DoctorResponse>
    updateDoctorStatus(
            @PathVariable Long doctorId,
            @RequestParam DoctorStatus status) {

        return ResponseEntity.ok(
                doctorService.updateDoctorStatus(
                        doctorId,
                        status));
    }


//    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping("/{doctorId}/consultation-fee")
    public ResponseEntity<DoctorResponse>
    updateConsultationFee(
            @PathVariable Long doctorId,
            @RequestParam BigDecimal fee) {

        return ResponseEntity.ok(
                doctorService.updateConsultationFee(
                        doctorId,
                        fee));
    }


    @GetMapping("/search/specialization")
    public ResponseEntity<List<DoctorResponse>>
    getDoctorsBySpecialization(
            @RequestParam String value) {

        return ResponseEntity.ok(
                doctorService
                        .getDoctorsBySpecialization(
                                value));
    }


    @GetMapping("/experience/{years}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsByExperience(
            @PathVariable Integer years) {

        return ResponseEntity.ok(
                doctorService.getDoctorsByMinimumExperience(years)
        );
    }


    @GetMapping("/fee-range")
    public List<DoctorResponse> getDoctorsByFeeRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {

        return doctorService
                .getDoctorsByConsultationFeeRange(min, max);
    }



//    @PreAuthorize("hasRole('DOCTOR')")
//    @PostMapping("/medicine-suggestion")
//    public ResponseEntity<MedicineSuggestionResponse>
//    generateMedicineSuggestion(
//            @Valid @RequestBody MedicineSuggestionRequest request) {
//
//        return ResponseEntity.ok(
//                medicineSuggestionService
//                        .generateSuggestion(request)
//        );
//    }
}