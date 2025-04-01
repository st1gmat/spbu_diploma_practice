package com.diploma.order_service.services;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Service;
import com.diploma.order_service.exceptions.BusinessException;
import com.diploma.order_service.models.order.Order;
import com.diploma.order_service.models.order.OrderConfirmation;
import com.diploma.order_service.models.order.OrderLine;
import com.diploma.order_service.models.order.OrderRequest;
import com.diploma.order_service.models.order.OrderResponse;
import com.diploma.order_service.models.order.PaymentMethod;
import com.diploma.order_service.models.payment.PaymentRequest;
import com.diploma.order_service.repository.OrderRepository;
import com.diploma.order_service.requests.CustomerClient;
import com.diploma.order_service.requests.PaymentClient;
import com.diploma.order_service.requests.ProductClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    // DevNotes: ****************************
    // проверить покупателя (существует ли?) = +
    // оформить товар, т.е. order-service -> product-service = +
    // сохранить заказ в бд = +
    // сохранить строки в заказе = +
    // реализовать процесс оплаты = +
    // подтверждение заказа (notification-service)
    // ****************************

// return customerClient.existsById(request.customerId())
        //         .filter(Boolean::booleanValue)
        //         .switchIfEmpty(Mono.error(new BusinessException("Customer not found")))
        //         .flatMap(customer -> {
        //             Order order = Order.builder()
        //                     .reference(request.reference())
        //                     .customerId(request.customerId())
        //                     .paymentMethod(request.paymentMethod().name())
        //                     .totalAmount(BigDecimal.ZERO)
        //                     .createdDate(Instant.now())
        //                     .lastModifiedDate(Instant.now())
        //                     .build();

        //             return orderRepository.save(order)
        //                     .flatMap(savedOrder -> orderLineService.createOrderLinesFromBuyRequests(savedOrder.getId(), request.products())
        //                             .collectList()
        //                             .flatMap(orderLines -> {
        //                                 BigDecimal total = orderLines.stream()
        //                                         .map(OrderLine::getQuantity)
        //                                         .reduce(BigDecimal.ZERO, BigDecimal::add);

        //                                 savedOrder.setTotalAmount(total);

        //                                 return orderRepository.save(savedOrder)
        //                                         .then(productClient.buy(request.products()))
        //                                         .then(paymentClient.pay(new PaymentRequest(
        //                                                 savedOrder.getTotalAmount(),
        //                                                 PaymentMethod.valueOf(savedOrder.getPaymentMethod()),
        //                                                 savedOrder.getId(),
        //                                                 savedOrder.getReference(),
        //                                                 customer)))
        //                                         .then(orderProducer.sendOrder(new OrderConfirmation(
        //                                                 savedOrder.getReference(),
        //                                                 savedOrder.getTotalAmount(),
        //                                                 PaymentMethod.valueOf(savedOrder.getPaymentMethod()),
        //                                                 null,
        //                                                 null)))
        //                                         .thenReturn(savedOrder.getId());
        //                             }));
        //         });

    public Mono<Integer> createOrder(OrderRequest request) { 
        
        // return customerClient.findById(request.customerId())
        //         .switchIfEmpty(Mono.error(new BusinessException("Customer not found")))
        //         .flatMap(customer -> {
        //                 Order order = Order.builder()
        //                         .reference(request.reference())
        //                         .customerId(request.customerId())
        //                         .paymentMethod(request.paymentMethod().name())
        //                         .totalAmount(BigDecimal.ZERO)
        //                         .createdDate(Instant.now())
        //                         .lastModifiedDate(Instant.now())
        //                         .build();
        //                 return orderRepository.save(order)
        //                         .flatMap(savedOrder -> orderLineService.createOrderLinesFromBuyRequests(savedOrder.getId(), request.products())
        //                                 .collectList()
        //                                 .flatMap(orderLines -> {
        //                                         BigDecimal total = orderLines.stream()
        //                                                 .map(OrderLine::getQuantity)
        //                                                 .reduce(BigDecimal.ZERO, BigDecimal::add);

        //                                         savedOrder.setTotalAmount(total);

        //                                         return orderRepository.save(savedOrder)
        //                                                 .then(productClient.buy(request.products()))
        //                                                 .flatMap((List<BuyResponse> responses) -> paymentClient.pay(new PaymentRequest(savedOrder.getTotalAmount(),
        //                                                 PaymentMethod.valueOf(savedOrder.getPaymentMethod()),
        //                                                 savedOrder.getId(),
        //                                                 savedOrder.getReference(),
        //                                                 customer))
        //                                                 .then(orderProducer.sendOrder(new OrderConfirmation(
        //                                                         orderReference, 
        //                                                         total,  
        //                                                         PaymentMethod.valueOf(savedOrder.getPaymentMethod()), 
        //                                                         customer, 
        //                                                         responses)))
        //                                                 .thenReturn(saved)
        //                                                 // .then(paymentClient.pay(new PaymentRequest(
        //                                                 //         savedOrder.getTotalAmount(),
        //                                                 //         PaymentMethod.valueOf(savedOrder.getPaymentMethod()),
        //                                                 //         savedOrder.getId(),
        //                                                 //         savedOrder.getReference(),
        //                                                 //         customer)))
        //                                                 // .then(orderProducer.sendOrder(new OrderConfirmation(
        //                                                 //         savedOrder.getReference(),
        //                                                 //         savedOrder.getTotalAmount(),
        //                                                 //         PaymentMethod.valueOf(savedOrder.getPaymentMethod()),
        //                                                 //         customer,
        //                                                 //         null)))
        //                                                 // .thenReturn(savedOrder.getId());
        //                                 }));   
        //         });
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
                            .build();

                    return orderRepository.save(order)
                            .flatMap(savedOrder -> orderLineService.createOrderLinesFromBuyRequests(savedOrder.getId(), request.products())
                                    .collectList()
                                    .flatMap(orderLines -> {
                                        BigDecimal total = orderLines.stream()
                                                .map(OrderLine::getQuantity)
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                                        savedOrder.setTotalAmount(total);

                                        return orderRepository.save(savedOrder)
                                                .flatMap(updatedOrder -> productClient.buy(request.products())
                                                        .flatMap(responses -> paymentClient.pay(new PaymentRequest(
                                                                        updatedOrder.getTotalAmount(),
                                                                        PaymentMethod.valueOf(updatedOrder.getPaymentMethod()),
                                                                        updatedOrder.getId(),
                                                                        updatedOrder.getReference(),
                                                                        customer))
                                                                .then(orderProducer.sendOrder(new OrderConfirmation(
                                                                        updatedOrder.getReference(),
                                                                        updatedOrder.getTotalAmount(),
                                                                        PaymentMethod.valueOf(updatedOrder.getPaymentMethod()),
                                                                        customer,
                                                                        responses)))
                                                                .thenReturn(updatedOrder.getId())));
                                    }));
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
