package com.bill_service.repository;

import com.bill_service.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository
        extends JpaRepository<Bill, Long> {

    List<Bill> findByPatientId(Long patientId);

    Optional<Bill> findByAppointmentId(Long appointmentId);
}