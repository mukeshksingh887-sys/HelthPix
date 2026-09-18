package com.doctor_service.service;

import com.doctor_service.dto.CreateDoctorRequest;
import com.doctor_service.dto.DoctorResponse;
import com.doctor_service.enitiy.DoctorStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    DoctorResponse updateDoctorStatus(
            Long doctorId,
            DoctorStatus status);

    DoctorResponse updateConsultationFee(
            Long doctorId,
            BigDecimal fee);

    List<DoctorResponse>
            getDoctorsBySpecialization(
            String specialization);

    List<DoctorResponse> getDoctorsByMinimumExperience(Integer years);

    List<DoctorResponse> getDoctorsByConsultationFeeRange(
            BigDecimal min,
            BigDecimal max);


//    public MedicineSuggestionResponse
//    getMedicineSuggestion(
//            MedicineSuggestionRequest request)
}
