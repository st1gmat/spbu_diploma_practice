package com.diploma.order_service.controller;

import com.diploma.order_service.models.order.OrderLineResponse;
import com.diploma.order_service.services.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/api/v1/order-lines")
@RequiredArgsConstructor
public class OrderLinesController {

    private final OrderLineService service;

    @GetMapping("/order/{order-id}")
    public Flux<OrderLineResponse> findById(@PathVariable("order-id") Integer orderId) {
        return service.findOrderLineById(orderId);
    }
}