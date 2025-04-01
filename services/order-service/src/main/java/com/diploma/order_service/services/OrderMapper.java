package com.diploma.order_service.services;

import org.springframework.stereotype.Service;

import com.diploma.order_service.models.order.Order;
import com.diploma.order_service.models.order.OrderRequest;
import com.diploma.order_service.models.order.OrderResponse;

@Service
public class OrderMapper {
    public Order toOrder(OrderRequest request) {
        return Order.builder()
        .id(request.id())
        .reference(request.reference())
        .totalAmount(request.requestedAmount())
        .paymentMethod(request.paymentMethod())
        .customerId(request.customerId())
        .build();
    }

    public OrderResponse fromOrder(Order order) {
        return new OrderResponse(order.getId(), order.getReference(), order.getTotalAmount(), order.getPaymentMethod(), order.getCustomerId());
    }
}
