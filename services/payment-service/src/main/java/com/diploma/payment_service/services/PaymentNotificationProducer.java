package com.diploma.payment_service.services;

import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import com.diploma.payment_service.models.PaymentNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentNotificationProducer {
    private final ReactiveKafkaProducerTemplate<String, PaymentNotificationRequest> kafkaTemplate;

    // dev_notes: тоже рабочая, но можно сократить
    // public Mono<Void> sendNotification(PaymentNotificationRequest request) {
    //     log.info("PaymentNotificationProducer :: Sending payment_notification to notif service :: {}", request);

    //     Message<PaymentNotificationRequest> message = MessageBuilder
    //             .withPayload(request)
    //             .setHeader(KafkaHeaders.TOPIC, "payment-topic")
    //             .build();

    //     return kafkaTemplate.send("payment-topic", request)
    //             .doOnSuccess(result -> log.info("PaymentNotificationProducer:: Notification sent successfully: {}", result.recordMetadata()))
    //             .doOnError(error -> log.error("PaymentNotificationProducer:: Failed to send notification", error))
    //             .then();
    // }
    public Mono<Void> sendNotification(PaymentNotificationRequest request) {
        log.info("PaymentNotificationProducer:: Sending payment_notification: {}", request);
    
        return kafkaTemplate.send("payment-topic", request)
                .doOnSuccess(result -> log.info("Notification sent successfully to partition {} at offset {}",
                        result.recordMetadata().partition(), result.recordMetadata().offset()))
                .doOnError(error -> log.error("Failed to send notification", error))
                .then();
    }
    
}
