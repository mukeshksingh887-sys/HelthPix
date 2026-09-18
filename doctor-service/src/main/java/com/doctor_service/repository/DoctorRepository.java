package com.doctor_service.repository;


import com.doctor_service.enitiy.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByDoctorId(Long userId);

    boolean existsByDoctorId(Long userId);

    boolean existsByLicenseNumber(String licenseNumber);

    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);

//    Optional<Doctor> findByDoctorEmail(String email);

    List<Doctor>
    findByConsultationFeeBetween(
            BigDecimal min,
            BigDecimal max);

    List<Doctor> findByExperienceYearsGreaterThanEqual(
            Integer years);

       Optional<Doctor>findDoctorByUserId(Long userId);
}