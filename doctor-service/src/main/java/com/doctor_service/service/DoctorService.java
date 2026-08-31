package com.doctor_service.service;

import com.doctor_service.dto.CreateDoctorRequest;
import com.doctor_service.dto.DoctorResponse;

import java.util.List;

public interface DoctorService {


    DoctorResponse createDoctor(
            CreateDoctorRequest request);

    DoctorResponse getDoctorById(Long doctorId);

    DoctorResponse getDoctorByUserId(Long userId);

    List<DoctorResponse> getAllDoctors();

    DoctorResponse updateDoctor(
            Long doctorId,
            CreateDoctorRequest request);

    void deleteDoctor(Long doctorId);
}
