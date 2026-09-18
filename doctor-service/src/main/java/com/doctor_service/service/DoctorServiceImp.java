package com.doctor_service.service;

import com.doctor_service.client.AiServiceClient;
import com.doctor_service.client.UserServiceClient;
import com.doctor_service.dto.CreateDoctorRequest;
import com.doctor_service.dto.DoctorResponse;
import com.doctor_service.dto.UserResponse;
import com.doctor_service.enitiy.Doctor;
import com.doctor_service.enitiy.DoctorStatus;
import com.doctor_service.exception.DeleteFaildException;
import com.doctor_service.exception.ResourceNotFoundException;
import com.doctor_service.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DoctorServiceImp implements DoctorService {

    @Autowired
    private  AiServiceClient aiServiceClient;

    @Autowired
    private  DoctorRepository doctorRepository;

    @Autowired
    private  UserServiceClient userServiceClient;

//    public MedicineSuggestionResponse
//    getMedicineSuggestion(
//            MedicineSuggestionRequest request) {
//
//        return aiServiceClient
//                .generateMedicineSuggestion(request);
//    }
//






    @Override
    public DoctorResponse createDoctor(CreateDoctorRequest request) {

        log.info(" user id  is : {}",request.getUserId());
        // 1. Validate request
        if (request == null) {
            throw new IllegalArgumentException(
                    "Create doctor request must not be null"
            );
        }

        if (request.getUserId() == null) {
            throw new IllegalArgumentException(
                    "User ID must not be null"
            );
        }

        // 2. Validate User exists in User Service
        UserResponse user = userServiceClient.getUserById(
                request.getUserId()
        );

        log.info("user: {}",user);

        if (user == null || user.getId() == null) {
//
            throw new ResourceNotFoundException("User not found with userId : " + request.getUserId());
        }

        // 3. Check whether doctor already exists for this user
        if (doctorRepository.existsByDoctorId(request.getUserId())) {
            throw new IllegalArgumentException(
                    "Doctor already exists for userId : "
                            + request.getUserId()
            );
        }

        // 4. Check duplicate license number
        if (doctorRepository.existsByLicenseNumber(
                request.getLicenseNumber())) {

            throw new IllegalArgumentException(
                    "License number already exists : "
                            + request.getLicenseNumber()
            );
        }

        // 5. Create Doctor entity
        Doctor doctor = Doctor.builder()
                .userId(user.getId())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .consultationFee(request.getConsultationFee())
                .department(request.getDepartment())
                .licenseNumber(request.getLicenseNumber())
                .status(DoctorStatus.AVAILABLE) // by default Avaliable
                .build();

        // 6. Save Doctor
        Doctor savedDoctor = doctorRepository.save(doctor);

        // 7. Convert Entity -> Response
        return mapToDoctorResponse(savedDoctor,user);
    }


    @Override
    @Transactional
    public DoctorResponse getDoctorById(Long doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id : "
                                        + doctorId));

        UserResponse user = userServiceClient.getUserById(
          doctor.getUserId()
        );


//        return mapToResponse(doctor);
        return mapToDoctorResponse(doctor,user);
    }

    @Override
    @Transactional
    public DoctorResponse getDoctorByUserId(Long userId) {

        Doctor doctor = doctorRepository.findDoctorByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found for userId : "
                                        + userId));

        return mapToResponse(doctor);
    }

    @Override
//    @Transactional
    public List<DoctorResponse> getAllDoctors() {


        List<Doctor> doctors = doctorRepository.findAll();

        List<DoctorResponse> responses = new ArrayList<>();

        for (Doctor doctor : doctors) {

            // Fetch user details from User Service
            UserResponse user = userServiceClient.getUserById(
                    doctor.getUserId()
            );

            // Map doctor + user
            DoctorResponse response = mapToDoctorResponse(
                    doctor,
                    user
            );

            responses.add(response);
        }

        return responses;

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

            throw new LayerInstantiationException(
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

//    @Override
//    public DoctorResponse getByDoctorEmail(String email) {
//
//     Doctor doctor = doctorRepository.findByDoctorEmail(email).orElseThrow(()->
//                new ResourceNotFoundException("Doctor not found with id : " + email));
//     return  mapToResponse(doctor);
//
//    }

    @Override
    @Transactional
    public void deleteDoctor(Long doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id : "
                                        + doctorId));


//        userServiceClient.deleteUser(doctor.getUserId());
//
//
//        doctorRepository.delete(doctor);

        try {

            userServiceClient.deleteUser(
                    doctor.getUserId());

            doctorRepository.delete(doctor);

        } catch (Exception ex) {

            throw new DeleteFaildException(
                    "Failed to delete user service record"
                    );
        }
    }





    @Override
    public DoctorResponse updateDoctorStatus(
            Long doctorId,
            DoctorStatus status) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found"));

        doctor.setStatus(status);

        Doctor updatedDoctor =
                doctorRepository.save(doctor);

        UserResponse user =
                userServiceClient.getUserById(
                        updatedDoctor.getUserId());

        return mapToDoctorResponse(
                updatedDoctor,
                user);
    }



    @Override
    public DoctorResponse updateConsultationFee(
            Long doctorId,
            BigDecimal fee) {

        Doctor doctor =
                doctorRepository.findById(
                                doctorId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found"));

        doctor.setConsultationFee(fee);

        Doctor updatedDoctor =
                doctorRepository.save(doctor);

        UserResponse user =
                userServiceClient.getUserById(
                        updatedDoctor.getUserId());

        return mapToDoctorResponse(
                updatedDoctor,
                user);
    }


    @Override
    public List<DoctorResponse>
    getDoctorsBySpecialization(
            String specialization) {

        List<Doctor> doctors =
                doctorRepository
                        .findBySpecializationContainingIgnoreCase(
                                specialization);

        return doctors.stream()
                .map(this::mapToResponse)
                .toList();
    }




    @Override
    public List<DoctorResponse> getDoctorsByMinimumExperience(Integer years) {

        List<Doctor> doctors =
                doctorRepository.findByExperienceYearsGreaterThanEqual(years);

        if (doctors.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No doctors found with experience greater than or equal to "
                            + years + " years");
        }

        return doctors.stream()
                .map(this::mapToResponse)
                .toList();
    }



    @Override
    public List<DoctorResponse> getDoctorsByConsultationFeeRange(
            BigDecimal min,
            BigDecimal max) {

        List<Doctor> doctors =
                doctorRepository.findByConsultationFeeBetween(min, max);

        return doctors.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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





    private DoctorResponse mapToDoctorResponse(
            Doctor doctor,
            UserResponse user) {

        return DoctorResponse.builder()
                .doctorId(doctor.getDoctorId())
                .userId(doctor.getUserId())
                .specialization(
                        doctor.getSpecialization()
                )
                .qualification(
                        doctor.getQualification()
                )
                .experienceYears(
                        doctor.getExperienceYears()
                )
                .consultationFee(
                        doctor.getConsultationFee()
                )
                .department(
                        doctor.getDepartment()
                )
                .licenseNumber(
                        doctor.getLicenseNumber()
                )
                .status(doctor.getStatus())
                .user(user)
                .build();
    }
}
