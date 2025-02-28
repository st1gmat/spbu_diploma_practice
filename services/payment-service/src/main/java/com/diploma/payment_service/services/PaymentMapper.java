package com.diploma.payment_service.services;

import org.springframework.stereotype.Service;

import com.diploma.payment_service.models.Payment;
import com.diploma.payment_service.models.PaymentRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentMapper {
    
    
    public Payment toPayment(PaymentRequest request) {
        return Payment.builder()
            .id(request.id())
            .amount(request.amount())
            .paymentMethod(request.paymentMethod())
            .build();
    }



}
