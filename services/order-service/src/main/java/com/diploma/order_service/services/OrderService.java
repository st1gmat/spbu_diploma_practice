package com.diploma.order_service.services;

import com.diploma.order_service.exceptions.BusinessException;
import com.diploma.order_service.models.order.*;
import com.diploma.order_service.models.payment.PaymentRequest;
import com.diploma.order_service.models.product.BuyRequestWrapper;
import com.diploma.order_service.repository.OrderRepository;
import com.diploma.order_service.requests.CustomerClient;
import com.diploma.order_service.requests.PaymentClient;
import com.diploma.order_service.requests.ProductClient;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;
    private final BuyRequestProducer buyRequestProducer;
    private final RetryRegistry retryRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public Mono<Integer> createOrder(OrderRequest request) {
        return customerClient.findById(request.customerId())
                .switchIfEmpty(Mono.error(new BusinessException("Customer not found")))
                .flatMap(customer -> {
                    Order order = Order.builder()
                            .reference(request.reference())
                            .customerId(request.customerId())
                            .paymentMethod(request.paymentMethod().name())
                            .totalAmount(BigDecimal.ZERO)
                            .createdDate(Instant.now())
                            .lastModifiedDate(Instant.now())
                            .status("PENDING")
                            .build();

                    return orderRepository.save(order)
                            .flatMap(savedOrder ->
                                    orderLineService.createOrderLinesFromBuyRequests(savedOrder.getId(), request.products())
                                            .collectList()
                                            .flatMap(orderLines -> {
                                                BigDecimal total = orderLines.stream()
                                                        .map(OrderLine::getQuantity)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                                savedOrder.setTotalAmount(total);

                                                return orderRepository.save(savedOrder)
                                                        .flatMap(updatedOrder -> {
                                                        //     var retry = retryRegistry.retry("productServiceRetry");
                                                        //     var circuitBreaker = circuitBreakerRegistry.circuitBreaker("productServiceCircuitBreaker");

                                                            return Mono.defer(() -> productClient.buy(request.products()))
                                                                //     .transformDeferred(RetryOperator.of(retry))
                                                                //     .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                                                                    .flatMap(responses -> Mono.just(new PaymentRequest(
                                                                                    updatedOrder.getTotalAmount(),
                                                                                    PaymentMethod.valueOf(updatedOrder.getPaymentMethod()),
                                                                                    updatedOrder.getId(),
                                                                                    updatedOrder.getReference(),
                                                                                    customer))
                                                                            .flatMap(paymentClient::pay)
                                                                            .then(orderProducer.sendOrder(new OrderConfirmation(
                                                                                    updatedOrder.getReference(),
                                                                                    updatedOrder.getTotalAmount(),
                                                                                    PaymentMethod.valueOf(updatedOrder.getPaymentMethod()),
                                                                                    customer,
                                                                                    responses)))
                                                                            .then(orderRepository.save(updatedOrder))
                                                                            .map(Order::getId))
                                                                    .onErrorResume(e -> buyRequestProducer.sendToQueue(new BuyRequestWrapper(request.products(), updatedOrder.getId(), 0))
                                                                            .thenReturn(updatedOrder.getId()));
                                                        });
                                            })
                            );
                });
    }

    public Mono<OrderResponse> findById(Integer id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException("Order not found")))
                .map(orderMapper::fromOrder);
    }

    public Flux<OrderResponse> findAll() {
        return orderRepository.findAll()
                .map(orderMapper::fromOrder);
    }
}