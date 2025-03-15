package com.diploma.payment_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diploma.payment_service.models.PaymentRequest;
import com.diploma.payment_service.services.PaymentService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public Mono<String> test() {
        return Mono.just("Server is working");
    }

    @PostMapping
    public Mono<Integer> createPayment(@RequestBody PaymentRequest payment) {
        return paymentService.createPayment(payment);
    }
}
