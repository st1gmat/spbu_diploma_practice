package com.diploma.order_service.controller;

import com.diploma.order_service.models.order.OrderRequest;
import com.diploma.order_service.models.order.OrderResponse;
import com.diploma.order_service.services.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping
    public Mono<Integer> createOrder(@RequestBody OrderRequest request) {
        return service.createOrder(request);
    }

    @GetMapping
    public Flux<OrderResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{order-id}")
    public Mono<OrderResponse> findById(@PathVariable("order-id") Integer orderId) {
        return service.findById(orderId);
    }
}
