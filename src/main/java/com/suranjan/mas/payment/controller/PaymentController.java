package com.suranjan.mas.payment.controller;

import com.suranjan.mas.payment.dto.PaymentResponse;
import com.suranjan.mas.payment.dto.PaymentVerificationRequest;
import com.suranjan.mas.payment.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create/{orderId}")
    public PaymentResponse createPayment(
            @PathVariable Long orderId
    ) throws Exception {

        return paymentService.createPaymentOrder(orderId);
    }

    @PostMapping("/verify")
    public String verifyPayment(
            @RequestBody PaymentVerificationRequest request
    ) {

        return paymentService.verifyPayment(request);
    }
}