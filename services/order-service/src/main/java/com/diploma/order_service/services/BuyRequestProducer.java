package com.diploma.order_service.services;

import com.diploma.order_service.models.product.BuyRequestWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.support.KafkaHeaders;
// import org.springframework.messaging.Message;
// import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuyRequestProducer {

    private final KafkaTemplate<String, BuyRequestWrapper> kafkaTemplate;

    public Mono<Void> sendToQueue(BuyRequestWrapper wrapper) {
        return Mono.fromRunnable(() -> {
            log.info("Kafka:: sending buy request to queue: {}", wrapper);
            kafkaTemplate.send("buy-request-topic", wrapper);
        });
    }
}
