package com.bill_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "PAYMENT-SERVICE")
public interface PaymentClient {

//    @PostMapping("/internal/payments")
//    PaymentResponse createPayment(
//            @RequestBody PaymentRequest request);
}