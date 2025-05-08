package com.diploma.order_service.services;

import com.diploma.order_service.models.order.OrderConfirmation;
import com.diploma.order_service.models.order.PaymentMethod;
import com.diploma.order_service.models.payment.PaymentRequest;
import com.diploma.order_service.models.product.BuyRequestWrapper;
import com.diploma.order_service.repository.OrderRepository;
import com.diploma.order_service.requests.CustomerClient;
import com.diploma.order_service.requests.PaymentClient;
import com.diploma.order_service.requests.ProductClient;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.reactor.bulkhead.operator.BulkheadOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class BuyRequestKafkaWorker {

    private static final int MAX_RETRIES = 4;

    private final ProductClient productClient;
    private final CustomerClient customerClient;
    private final PaymentClient paymentClient;
    private final OrderProducer orderProducer;
    private final DlqProducer dlqProducer;
    private final OrderRepository orderRepository;
    private final BuyRequestProducer buyRequestProducer;
    private final BulkheadRegistry bulkheadRegistry;

    @KafkaListener(topics = "buy-request-topic", groupId = "buy-request-group")
    public void listen(BuyRequestWrapper wrapper) {
        log.info("Kafka:: received buy request: {}", wrapper);

        Bulkhead bulkhead = bulkheadRegistry.bulkhead("paymentClientBulkhead");

        productClient.buy(wrapper.requests())
            .flatMap(responses ->
                orderRepository.findById(wrapper.orderId())
                    .flatMap(order -> customerClient.findById(order.getCustomerId())
                        .flatMap(customer -> {
                            order.setStatus("CONFIRMED");
                            PaymentRequest paymentRequest = new PaymentRequest(
                                    order.getTotalAmount(),
                                    PaymentMethod.valueOf(order.getPaymentMethod()),
                                    order.getId(),
                                    order.getReference(),
                                    customer
                            );
                            return Mono.defer(() -> paymentClient.pay(paymentRequest))
                                .transformDeferred(BulkheadOperator.of(bulkhead))
                                .then(orderProducer.sendOrder(new OrderConfirmation(
                                        order.getReference(),
                                        order.getTotalAmount(),
                                        PaymentMethod.valueOf(order.getPaymentMethod()),
                                        customer,
                                        responses)))
                                .then(orderRepository.save(order));
                        })
                    )
            )
            .doOnError(e -> {
                BuyRequestWrapper next = wrapper.incrementAttempt();
                if (next.attempt() >= MAX_RETRIES) {
                    log.warn("Max retries exceeded. Sending to DLQ: {}", next);
                    dlqProducer.sendToDlq(next).subscribe();
                    orderRepository.findById(next.orderId())
                        .flatMap(order -> {
                            order.setStatus("FAILED");
                            return orderRepository.save(order);
                        }).subscribe();
                } else {
                    log.warn("Retrying buy request, attempt #{}", next.attempt());
                    buyRequestProducer.sendToQueue(next).subscribe();
                }
            })
            .subscribe();
    }
}
