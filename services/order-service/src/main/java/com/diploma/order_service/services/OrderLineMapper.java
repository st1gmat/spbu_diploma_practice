package com.diploma.order_service.services;

import org.springframework.stereotype.Service;

import com.diploma.order_service.models.order.Order;
import com.diploma.order_service.models.order.OrderLine;
import com.diploma.order_service.models.order.OrderLineRequest;
import com.diploma.order_service.models.order.OrderLineResponse;

@Service
public class OrderLineMapper {

    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
            .id(request.id())
            .quantity(request.quantity())
            .order(Order.builder()
                .id(request.orderId())
                .build()
            )
            .productId(request.productId())
            .build();
    }
    public OrderLineResponse toOrderLineResponse(OrderLine request) {
        return new OrderLineResponse(request.getId(), request.getQuantity());
    }

}
