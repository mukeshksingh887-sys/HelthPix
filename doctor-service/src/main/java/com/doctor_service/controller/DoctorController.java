package com.doctor_service.controller;

import com.doctor_service.dto.CreateDoctorRequest;
import com.doctor_service.dto.DoctorResponse;
import com.doctor_service.enitiy.DoctorStatus;
import com.doctor_service.security.UserPrincipal;
import com.doctor_service.service.DoctorService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    @Autowired
    private DoctorService doctorService;


    @GetMapping("/public")
    public ResponseEntity< String> get(){

         return  ResponseEntity.ok().body(" i am for doctor public api ");

    }

    /**
     * Create Doctor
     */
    @PreAuthorize("hasRole('DOCTOR')")
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
    @GetMapping("/public/get/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorById(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                doctorService.getDoctorById(doctorId)
        );
    }

    /**
     * Get Doctor By User Id
     */
    @GetMapping("/public/userbyuser/{userId}")
    public ResponseEntity<DoctorResponse> getDoctorByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                doctorService.getDoctorByUserId(userId)
        );
    }

    /**
     * Get All Doctors
     */
    @GetMapping("/public/getAllDoctor")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {

        return ResponseEntity.ok(
                doctorService.getAllDoctors()
        );
    }

    /**
     * Update Doctor
     */
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
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
  @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @DeleteMapping("delete/{doctorId}")
    public ResponseEntity<String> deleteDoctor(
            @PathVariable Long doctorId) {

        doctorService.deleteDoctor(doctorId);

        return ResponseEntity.ok(
                "Doctor deleted successfully"
        );
    }


// change doctor status
@PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
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


    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
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


    // change Specilazation
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/search/specialization")
    public ResponseEntity<List<DoctorResponse>>
    getDoctorsBySpecialization(
            @RequestParam String value) {

        return ResponseEntity.ok(
                doctorService
                        .getDoctorsBySpecialization(
                                value));
    }

// change Exprience years
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/experience/{years}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsByExperience(
            @PathVariable Integer years) {

        return ResponseEntity.ok(
                doctorService.getDoctorsByMinimumExperience(years)
        );
    }

   // get doctor by fee range
    @GetMapping("/public/fee-range")
    public List<DoctorResponse> getDoctorsByFeeRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {

        return doctorService
                .getDoctorsByConsultationFeeRange(min, max);
    }





    @GetMapping("/me")
    public UserPrincipal currentUser(
            @AuthenticationPrincipal
            UserPrincipal user) {

        return user;
    }

    @PostMapping("/loged/create")
    @PreAuthorize("hasRole('ADMIN','DOCTOR')")
    public DoctorResponse createDoctor(
            @RequestBody CreateDoctorRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        System.out.println(currentUser.getUserId());
        System.out.println(currentUser.getEmail());
        System.out.println(currentUser.getRole());

        return doctorService.createDoctor(request);
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