package com.doctor_service.repository;


import com.doctor_service.enitiy.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByDoctorId(Long userId);

    boolean existsByDoctorId(Long userId);

    boolean existsByLicenseNumber(String licenseNumber);

//    Optional<Doctor> findByDoctorEmail(String email);
}