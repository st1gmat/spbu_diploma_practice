package com.diploma.order_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.diploma.order_service.models.order.OrderLineRequest;
import com.diploma.order_service.models.order.OrderLineResponse;
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

    public List<OrderLineResponse> findOrderLineById(Integer orderId) {
        return orderLineRepository.findAllByOrderId(orderId).stream().map(orderLineMapper::toOrderLineResponse).collect(Collectors.toList());
    }

}
