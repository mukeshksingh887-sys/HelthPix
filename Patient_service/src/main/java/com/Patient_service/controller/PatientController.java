package com.Patient_service.controller;


import com.Patient_service.dto.PatientRequest;
import com.Patient_service.dto.PatientResponse;
import com.Patient_service.entity.Patient;
import com.Patient_service.service.PatientService;
import com.Patient_service.util.PatientStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {


    @Autowired
  private   PatientService patientService;
//        @GetMapping("check")
//     public  String check(){
//         return " server from patient service";
//     }

    @PostMapping("/create")
    public ResponseEntity<PatientResponse> createPatient(
            @RequestBody PatientRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(patientService.createPatient(request));
    }


    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                patientService.getPatientById(id)
        );
    }

    @GetMapping("/getAllPatient")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {

        return ResponseEntity.ok(
                patientService.getAllPatients()
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientRequest request) {

        return ResponseEntity.ok(
                patientService.updatePatient(id, request)
        );
    }


    @PatchMapping("/{id}")
    public ResponseEntity<PatientResponse> patchPatient(
            @PathVariable Long id,
            @RequestBody PatientRequest request) {

        return ResponseEntity.ok(patientService.PartialPatientUpdate(id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<PatientResponse> deletePatient(
            @PathVariable Long id) {



        return ResponseEntity.ok(patientService.deletePatient(id));
    }

//    // UPDATE STATUS
//    @PatchMapping("/Status/{id}")
//    public ResponseEntity<PatientResponse> updateStatus(
//            @PathVariable Long id,
//            @RequestParam PatientStatus status) {
//
//        return ResponseEntity.ok(
//                patientService.updateStatus(id, status)
//        );
//    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PatientResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody PatientRequest request) {

        return ResponseEntity.ok(
                patientService.updateStatus(id, PatientStatus.valueOf(request.getStatus()))
        );
    }


    @GetMapping("/status")
    public ResponseEntity<List<PatientResponse>> getPatientsByStatus(
            @RequestParam String status) {

        return ResponseEntity.ok(
                patientService.getPatientsByStatus(status)
        );
    }



    // SEARCH
    @GetMapping("/search")
    public ResponseEntity<List<PatientResponse>> searchPatients(
            @RequestParam String keyword) {

        List<PatientResponse> patients =
                patientService.searchPatients(keyword);

        return ResponseEntity.ok(patients);
    }
}
