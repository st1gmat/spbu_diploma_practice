package com.diploma.notification_service.services;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.diploma.notification_service.models.Notification;
import com.diploma.notification_service.models.NotificationType;
import com.diploma.notification_service.models.OrderConfirmation;
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
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) {
        log.info("NotificationConsumer:: consumePaymentSuccessNotification:: consuming message from payment-topic:: " + paymentConfirmation);
        repository.save(
            Notification.builder()
                .notificationType(NotificationType.PAYMENT_CONFIRMATION)
                .notificationDate(LocalDateTime.now())
                .paymentConfirmation(paymentConfirmation)
            .build()
        );

        var customerName = paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName();
        emailService.sendPaymentSuccessEmail(
                paymentConfirmation.email(),
                customerName,
                paymentConfirmation.quantity(),
                paymentConfirmation.orderRef()
        );
    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderSuccessNotification(OrderConfirmation orderConfirmation) {
        log.info("NotificationConsumer:: consumeOrderSuccessNotification:: consuming message from order-topic:: " + orderConfirmation);
        repository.save(
            Notification.builder()
                .notificationType(NotificationType.ORDER_CONFIRMATION)
                .notificationDate(LocalDateTime.now())
                .orderConfirmation(orderConfirmation)
            .build()
        );

        var customerName = orderConfirmation.customer().firstName() + " " + orderConfirmation.customer().lastName();
        emailService.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                customerName,
                orderConfirmation.quantity(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
    }

}
