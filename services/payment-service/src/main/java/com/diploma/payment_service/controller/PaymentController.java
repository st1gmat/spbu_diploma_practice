package com.diploma.payment_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diploma.payment_service.models.PaymentRequest;
import com.diploma.payment_service.services.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public String test() {
        return new String("Server is working");
    }
    

    @PostMapping
    public ResponseEntity<Integer> createPayment(@RequestBody @Valid PaymentRequest payment) {
        return ResponseEntity.ok(paymentService.createPayment(payment));
    }
    

}
