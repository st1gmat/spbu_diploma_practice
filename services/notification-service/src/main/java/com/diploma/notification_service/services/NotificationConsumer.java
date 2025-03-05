package com.diploma.notification_service.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.diploma.notification_service.models.PaymentConfirmation;
import com.diploma.notification_service.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    // dev notes ***********
    // 1) создать консюмера, который принимает сообщения из payment-topic (payment-confirmation)
    // 2) создать консюмера, который принимает сообщения из order-topic (order-confirmation)
    // 3) реализовать псевдоотправку email
    // *********************
    private final NotificationRepository repository;

    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) {
        // todo: implement class
        return;

    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderSuccessNotification(PaymentConfirmation paymentConfirmation) {
        // todo: implement class
        return;

    }

}
