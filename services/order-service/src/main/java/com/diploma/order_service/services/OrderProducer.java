package com.diploma.order_service.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.diploma.order_service.models.order.OrderConfirmation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {
    
    private KafkaTemplate<String, OrderConfirmation> kafkaTemplate;
    
    public void sendOrderConfirmation(OrderConfirmation orderConfirmation) {
        log.info("Kafka:: OrederProducer:: Sending order info to order-topic:" + orderConfirmation.toString());
        Message<OrderConfirmation> message = 
                    MessageBuilder
                    .withPayload(orderConfirmation)
                    .setHeader(KafkaHeaders.TOPIC, "order-topic")
                    .build();
        kafkaTemplate.send(message);
        
    }

}