package com.bill_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;

    private Long appointmentId;

    private Long doctorId;

    private BigDecimal consultationFee;

    private BigDecimal medicineCharge;

    private BigDecimal labCharge;

    private BigDecimal roomCharge;

    private BigDecimal procedureCharge;

    private BigDecimal discount;

    private BigDecimal tax;

    private BigDecimal totalAmount;

    private BigDecimal paidAmount;

    private BigDecimal dueAmount;

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    private LocalDateTime createdAt;

    // getters and setters
}