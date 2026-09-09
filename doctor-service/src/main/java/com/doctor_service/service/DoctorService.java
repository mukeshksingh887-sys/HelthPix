package com.doctor_service.service;

import com.doctor_service.dto.CreateDoctorRequest;
import com.doctor_service.dto.DoctorResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DoctorService {


    DoctorResponse createDoctor(
            CreateDoctorRequest request);

    DoctorResponse getDoctorById(Long doctorId);

    DoctorResponse getDoctorByUserId(Long userId);

    List<DoctorResponse> getAllDoctors();

    DoctorResponse updateDoctor(Long doctorId, CreateDoctorRequest request);

//    DoctorResponse getByDoctorEmail(String email);
    void deleteDoctor(Long doctorId);
}
