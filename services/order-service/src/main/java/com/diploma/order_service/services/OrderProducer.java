package com.diploma.order_service.services;

import com.diploma.order_service.models.order.OrderConfirmation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

    public Mono<Void> sendOrder(OrderConfirmation orderConfirmation) {
        return Mono.fromRunnable(() -> {
            log.info("Kafka:: Sending order info to order-topic: {}", orderConfirmation);
            Message<OrderConfirmation> message = MessageBuilder
                    .withPayload(orderConfirmation)
                    .setHeader(KafkaHeaders.TOPIC, "order-topic")
                    .build();
            kafkaTemplate.send(message);
        });
    }
}
