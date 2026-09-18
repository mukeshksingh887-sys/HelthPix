package com.bill_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateBillRequest {

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

    // getters and setters
}