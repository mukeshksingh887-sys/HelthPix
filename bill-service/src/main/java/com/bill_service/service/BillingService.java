package com.bill_service.service;


import com.bill_service.dto.CreateBillRequest;
import com.bill_service.dto.PaymentRequest;
import com.bill_service.entity.Bill;
import com.bill_service.entity.BillStatus;
import com.bill_service.repository.BillRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

    private final BillRepository billRepository;

    public BillingService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill createBill(CreateBillRequest request) {

        Bill bill = new Bill();
        bill.setPatientId(request.getPatientId());
        bill.setAppointmentId(request.getAppointmentId());
        bill.setDoctorId(request.getDoctorId());

        bill.setConsultationFee(
                defaultZero(request.getConsultationFee()));

        bill.setMedicineCharge(
                defaultZero(request.getMedicineCharge()));

        bill.setLabCharge(
                defaultZero(request.getLabCharge()));

        bill.setRoomCharge(
                defaultZero(request.getRoomCharge()));

        bill.setProcedureCharge(
                defaultZero(request.getProcedureCharge()));

        bill.setDiscount(
                defaultZero(request.getDiscount()));

        bill.setTax(
                defaultZero(request.getTax()));

        BigDecimal subtotal =
                bill.getConsultationFee()
                        .add(bill.getMedicineCharge())
                        .add(bill.getLabCharge())
                        .add(bill.getRoomCharge())
                        .add(bill.getProcedureCharge());

        BigDecimal total =
                subtotal
                        .subtract(bill.getDiscount())
                        .add(bill.getTax());

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Total amount cannot be negative");
        }

        bill.setTotalAmount(total);
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setDueAmount(total);

        bill.setStatus(BillStatus.GENERATED);
        bill.setCreatedAt(LocalDateTime.now());

        return billRepository.save(bill);
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }


    public Bill getBill(Long id) {

        return billRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Bill not found"));
    }

    public List<Bill> getBillsByPatient(Long patientId) {

        return billRepository.findByPatientId(patientId);
    }





    @Transactional
    public Bill makePayment(
            Long billId,
            PaymentRequest request) {

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() ->
                        new RuntimeException("Bill not found"));

        BigDecimal amount = request.getAmount();

        Optional<Bill> existing =
                billRepository.findByAppointmentId(
                        request.getAppointmentId());

        if (existing.isPresent()) {
            throw new RuntimeException(
                    "Bill already exists for appointment");
        }


        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero");
        }

        if (amount.compareTo(bill.getDueAmount()) > 0) {

            throw new IllegalArgumentException(
                    "Payment cannot exceed due amount");
        }

        BigDecimal paid =
                bill.getPaidAmount().add(amount);

        BigDecimal due =
                bill.getTotalAmount().subtract(paid);

        bill.setPaidAmount(paid);
        bill.setDueAmount(due);

        if (due.compareTo(BigDecimal.ZERO) == 0) {
            bill.setStatus(BillStatus.PAID);
        } else {
            bill.setStatus(BillStatus.PARTIALLY_PAID);
        }

        return billRepository.save(bill);
    }

}