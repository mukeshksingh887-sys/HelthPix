package com.Patient_service.controller;


import com.Patient_service.dto.PatientResponse;
import com.Patient_service.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/internal/patients")
public class PatientInternalController {
    @Autowired
    private   PatientService patientService;


//    @GetMapping("/email/{email}")
//    public PatientResponse getByEmail(
//            @PathVariable String email) {
//
//        return patientService.getPatientByEmail(email);
//    }
//   /internal/patients/{patientId}
    @GetMapping("/{patientId}")
    public  PatientResponse getById(@PathVariable("patientId") Long id){
        return patientService.getPatientById(id);
    }


}
