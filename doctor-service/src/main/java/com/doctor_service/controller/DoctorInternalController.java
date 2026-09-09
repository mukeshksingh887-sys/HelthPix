package com.doctor_service.controller;


import com.doctor_service.dto.DoctorResponse;
import com.doctor_service.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/doctors")
public class DoctorInternalController {

    @Autowired
   private DoctorService doctorService;

//    @GetMapping("/{email}")
//    public DoctorResponse getDoctorByEmail(@PathVariable String email){
//        return  doctorService.getByDoctorEmail(email);
//    }

//    @GetMapping("/{id}")
//    public  DoctorResponse getDoctorById(@PathVariable("id") Long id){
//        return doctorService.getDoctorById(id);
//    };


    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorById(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                doctorService.getDoctorById(doctorId)
        );
    }

}
