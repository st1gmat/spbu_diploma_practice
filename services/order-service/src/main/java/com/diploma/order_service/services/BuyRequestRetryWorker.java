// package com.diploma.order_service.services;

// import com.diploma.order_service.models.order.OrderConfirmation;
// import com.diploma.order_service.models.order.PaymentMethod;
// import com.diploma.order_service.models.payment.PaymentRequest;
// import com.diploma.order_service.models.product.BuyRequestWrapper;
// import com.diploma.order_service.repository.OrderRepository;
// import com.diploma.order_service.requests.CustomerClient;
// import com.diploma.order_service.requests.PaymentClient;
// import com.diploma.order_service.requests.ProductClient;
// import jakarta.annotation.PostConstruct;
// import lombok.RequiredArgsConstructor;
// import org.springframework.data.redis.core.ReactiveRedisTemplate;
// import org.springframework.stereotype.Component;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;

// import java.time.Duration;

// @Component
// @RequiredArgsConstructor
// public class BuyRequestRetryWorker {

//     private static final int MAX_RETRIES = 4;

//     private final ReactiveRedisTemplate<String, BuyRequestWrapper> buyRequestQueueTemplate;
//     private final ProductClient productClient;
//     private final CustomerClient customerClient;
//     private final PaymentClient paymentClient;
//     private final OrderProducer orderProducer;
//     private final DlqProducer dlqProducer;
//     private final OrderRepository orderRepository;

//     @PostConstruct
//     public void startRetry() {
//         Flux.interval(Duration.ofSeconds(10))
//             .flatMap(tick -> buyRequestQueueTemplate.opsForList().leftPop("buy-queue"))
//             .flatMap(wrapper -> productClient.buy(wrapper.requests())
//                 .flatMap(responses ->
//                     orderRepository.findById(wrapper.orderId())
//                         .flatMap(order -> customerClient.findById(order.getCustomerId())
//                             .flatMap(customer -> {
//                                 order.setStatus("CONFIRMED");

//                                 PaymentRequest paymentRequest = new PaymentRequest(
//                                         order.getTotalAmount(),
//                                         PaymentMethod.valueOf(order.getPaymentMethod()),
//                                         order.getId(),
//                                         order.getReference(),
//                                         customer
//                                 );

//                                 return paymentClient.pay(paymentRequest)
//                                     .then(orderProducer.sendOrder(new OrderConfirmation(
//                                             order.getReference(),
//                                             order.getTotalAmount(),
//                                             PaymentMethod.valueOf(order.getPaymentMethod()),
//                                             customer,
//                                             responses)))
//                                     .then(orderRepository.save(order));
//                             })
//                         )
//                 )
//                 .onErrorResume(e -> {
//     BuyRequestWrapper next = wrapper.incrementAttempt();
//     if (next.attempt() >= MAX_RETRIES) {
//         // Найдём заказ и обновим статус
//         return orderRepository.findById(wrapper.orderId())
//             .flatMap(order -> {
//                 order.setStatus("FAILED");
//                 return orderRepository.save(order);
//             })
//             .then(dlqProducer.sendToDlq(next))
//             .then(Mono.empty());
//     } else {
//         return buyRequestQueueTemplate.opsForList()
//             .rightPush("buy-queue", next)
//             .then(Mono.empty());
//     }
// })
//             )
//             .subscribe();
//     }
// }


package com.diploma.order_service.services;

import com.diploma.order_service.models.order.OrderConfirmation;
import com.diploma.order_service.models.order.PaymentMethod;
import com.diploma.order_service.models.payment.PaymentRequest;
import com.diploma.order_service.models.product.BuyRequestWrapper;
import com.diploma.order_service.repository.OrderRepository;
import com.diploma.order_service.requests.CustomerClient;
import com.diploma.order_service.requests.PaymentClient;
import com.diploma.order_service.requests.ProductClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
@Slf4j
public class BuyRequestRetryWorker {

    private static final int MAX_RETRIES = 4;

    private static final Duration FAST_INTERVAL = Duration.ofMillis(50);
    private static final Duration SLOW_INTERVAL = Duration.ofSeconds(7);

    private final ReactiveRedisTemplate<String, BuyRequestWrapper> buyRequestQueueTemplate;
    private final ProductClient productClient;
    private final CustomerClient customerClient;
    private final PaymentClient paymentClient;
    private final OrderProducer orderProducer;
    private final DlqProducer dlqProducer;
    private final OrderRepository orderRepository;

    private final AtomicReference<Duration> pollingInterval = new AtomicReference<>(SLOW_INTERVAL);

    @PostConstruct
    public void startRetry() {
        scheduleNext();
    }

    private void scheduleNext() {
        Mono.delay(pollingInterval.get())
            .flatMap(tick -> buyRequestQueueTemplate.opsForList().leftPop("buy-queue"))
            .flatMap(wrapper -> {
                if (wrapper == null) {
                    pollingInterval.set(SLOW_INTERVAL); // ничего не было — замедляемся
                    return Mono.empty();
                }

                return productClient.buy(wrapper.requests())
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

                                    return paymentClient.pay(paymentRequest)
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
                    .doOnSuccess(success -> pollingInterval.set(FAST_INTERVAL))
                    .onErrorResume(e -> {
                        BuyRequestWrapper next = wrapper.incrementAttempt();

                        if (next.attempt() >= MAX_RETRIES) {
                            log.warn("Max attempts exceeded for orderId={}, sending to DLQ", wrapper.orderId());
                            return orderRepository.findById(wrapper.orderId())
                                .flatMap(order -> {
                                    order.setStatus("FAILED");
                                    return orderRepository.save(order);
                                })
                                .then(dlqProducer.sendToDlq(next))
                                .then(Mono.empty());
                        } else {
                            pollingInterval.set(SLOW_INTERVAL); // при ошибке — замедляемся
                            return buyRequestQueueTemplate.opsForList()
                                .rightPush("buy-queue", next)
                                .then(Mono.empty());
                        }
                    });
            })
            .doOnTerminate(this::scheduleNext) // рекурсивно планируем следующее выполнение
            .subscribe();
    }
}
