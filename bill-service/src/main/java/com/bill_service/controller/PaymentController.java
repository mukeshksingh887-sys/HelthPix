package com.bill_service.controller;

import com.bill_service.dto.PaymentRequest;
import com.bill_service.entity.Bill;
import com.bill_service.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class PaymentController {

    @Autowired
   BillingService billingService;
    @PostMapping("/{billId}/payment")
    public ResponseEntity<Bill> makePayment(
            @PathVariable Long billId,
            @RequestBody PaymentRequest request) {

        return ResponseEntity.ok(
                billingService.makePayment(
                        billId,
                        request));
    }
}
