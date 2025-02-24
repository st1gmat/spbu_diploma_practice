package com.diploma.order_service.services;

import org.springframework.stereotype.Service;

import com.diploma.order_service.models.order.OrderLineRequest;
import com.diploma.order_service.repository.OrderLineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;

    public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
        var order = orderLineMapper.toOrderLine(orderLineRequest);
        return orderLineRepository.save(order).getId();
    }

}
