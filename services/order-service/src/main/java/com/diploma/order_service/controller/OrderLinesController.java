package com.diploma.order_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diploma.order_service.models.order.OrderLineResponse;
import com.diploma.order_service.services.OrderLineService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(path = "/api/v1/order-lines")
@RequiredArgsConstructor
public class OrderLinesController {

    private final OrderLineService service;

    @GetMapping("/order/{order-id}")
    public ResponseEntity<List<OrderLineResponse>> findById(@PathVariable("order-id") Integer orderId) {
        return ResponseEntity.ok(service.findOrderLineById(orderId));
    }
    

}
