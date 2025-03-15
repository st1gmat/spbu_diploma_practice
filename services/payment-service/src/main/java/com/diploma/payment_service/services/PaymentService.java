package com.diploma.payment_service.services;

import org.springframework.stereotype.Service;

import com.diploma.payment_service.models.PaymentNotificationRequest;
import com.diploma.payment_service.models.PaymentRequest;
import com.diploma.payment_service.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentNotificationProducer paymentNotificationProducer;

    public Mono<Integer> createPayment(PaymentRequest request) {

        return paymentRepository.save(paymentMapper.toPayment(request))
            .flatMap(payment -> {
                PaymentNotificationRequest notification = new PaymentNotificationRequest(
                        request.orderRef(),
                        request.amount(),
                        request.paymentMethod(),
                        request.customer().firstName(),
                        request.customer().lastName(),
                        request.customer().email());

                return paymentNotificationProducer.sendNotification(notification)
                        .thenReturn(payment.getId());
            });
    }

}