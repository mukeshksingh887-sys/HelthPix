package com.bill_service.controller;

import com.bill_service.dto.CreateBillRequest;
import com.bill_service.dto.PaymentRequest;
import com.bill_service.entity.Bill;
import com.bill_service.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private final BillingService billingService;

    public BillController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/createBill")
    public ResponseEntity<Bill> createBill(
            @RequestBody CreateBillRequest request) {

        Bill bill = billingService.createBill(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bill);
    }

    @GetMapping("getBill/{id}")
    public ResponseEntity<Bill> getBill(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                billingService.getBill(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Bill>> getBillsByPatient(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                billingService.getBillsByPatient(patientId));
    }


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
