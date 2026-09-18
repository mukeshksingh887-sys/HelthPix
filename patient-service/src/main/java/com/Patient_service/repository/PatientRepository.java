package com.Patient_service.repository;

import com.Patient_service.entity.Patient;
import com.Patient_service.entity.PatientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface PatientRepository extends JpaRepository<Patient,Long> {
    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<Patient> findByStatus(PatientStatus status);

    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName
    );

    List<Patient> findByBloodGroup(String bloodGroup);


//    @Query("""
//       SELECT p FROM Patient p
//       WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
//          OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
//          OR LOWER(p.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
//          OR p.phone LIKE CONCAT('%', :keyword, '%')
//       """)
//    List<Patient> searchPatients(String keyword);


}
