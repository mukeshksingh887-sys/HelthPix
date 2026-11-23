package com.Patient_service.repository;

import com.Patient_service.entity.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory,Long> {

    List<MedicalHistory> findByPatient_PatientId(Long patientId);

    void deleteByPatient_PatientId(Long patientId);

    boolean existsByPatient_PatientId(Long patientId);
}
