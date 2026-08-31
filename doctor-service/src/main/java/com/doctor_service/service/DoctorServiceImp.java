package com.doctor_service.service;

import com.doctor_service.client.UserServiceClient;
import com.doctor_service.dto.CreateDoctorRequest;
import com.doctor_service.dto.DoctorResponse;
import com.doctor_service.enitiy.Doctor;
import com.doctor_service.enitiy.DoctorStatus;
import com.doctor_service.exception.ResourceNotFoundException;
import com.doctor_service.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DoctorServiceImp implements DoctorService {

    @Autowired
    private  DoctorRepository doctorRepository;
    private  UserServiceClient userServiceClient;

    @Override
    public DoctorResponse createDoctor(CreateDoctorRequest request) {

        // Validate User Exists
        userServiceClient.getUserById(request.getUserId());

        if (doctorRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException(
                    "Doctor already exists for userId : "
                            + request.getUserId());
        }

        if (doctorRepository.existsByLicenseNumber(
                request.getLicenseNumber())) {

            throw new IllegalArgumentException(
                    "License number already exists : "
                            + request.getLicenseNumber());
        }

        Doctor doctor = Doctor.builder()
                .userId(request.getUserId())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .consultationFee(request.getConsultationFee())
                .department(request.getDepartment())
                .licenseNumber(request.getLicenseNumber())
                .status(DoctorStatus.AVAILABLE)
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);

        return mapToResponse(savedDoctor);
    }



    @Override
    @Transactional
    public DoctorResponse getDoctorById(Long doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id : "
                                        + doctorId));

        return mapToResponse(doctor);
    }

    @Override
    @Transactional
    public DoctorResponse getDoctorByUserId(Long userId) {

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found for userId : "
                                        + userId));

        return mapToResponse(doctor);
    }

    @Override
    @Transactional
    public List<DoctorResponse> getAllDoctors() {

        return doctorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public DoctorResponse updateDoctor(
            Long doctorId,
            CreateDoctorRequest request) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id : "
                                        + doctorId));

        if (!doctor.getLicenseNumber()
                .equals(request.getLicenseNumber())
                && doctorRepository.existsByLicenseNumber(
                request.getLicenseNumber())) {

            throw new IllegalArgumentException(
                    "License number already exists");
        }

        doctor.setSpecialization(
                request.getSpecialization());

        doctor.setQualification(
                request.getQualification());

        doctor.setExperienceYears(
                request.getExperienceYears());

        doctor.setConsultationFee(
                request.getConsultationFee());

        doctor.setDepartment(
                request.getDepartment());

        doctor.setLicenseNumber(
                request.getLicenseNumber());

        Doctor updatedDoctor =
                doctorRepository.save(doctor);

        return mapToResponse(updatedDoctor);
    }

    @Override
    public void deleteDoctor(Long doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id : "
                                        + doctorId));

        doctorRepository.delete(doctor);
    }

    private DoctorResponse mapToResponse(
            Doctor doctor) {

        return DoctorResponse.builder()
                .doctorId(doctor.getDoctorId())
                .userId(doctor.getUserId())
                .specialization(
                        doctor.getSpecialization())
                .qualification(
                        doctor.getQualification())
                .experienceYears(
                        doctor.getExperienceYears())
                .consultationFee(
                        doctor.getConsultationFee())
                .department(
                        doctor.getDepartment())
                .licenseNumber(
                        doctor.getLicenseNumber())
                .status(
                        doctor.getStatus())
                .build();
    }
}
