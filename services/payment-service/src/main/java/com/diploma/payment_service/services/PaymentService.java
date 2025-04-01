package com.diploma.payment_service.services;

import org.springframework.stereotype.Service;

import com.diploma.payment_service.models.PaymentNotificationRequest;
import com.diploma.payment_service.models.PaymentRequest;
import com.diploma.payment_service.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentNotificationProducer paymentNotificationProducer;

    public Integer createPayment(PaymentRequest request) {
        var payment = paymentRepository.save(paymentMapper.toPayment(request));

        paymentNotificationProducer.sendNotification(
                new PaymentNotificationRequest(
                        request.orderRef(),
                        request.amount(),
                        request.paymentMethod(),
                        request.customer().firstName(),
                        request.customer().lastName(),
                        request.customer().email()));

        return payment.getId();
    }

}