package com.diploma.order_service.services;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.diploma.order_service.models.order.OrderLine;
import com.diploma.order_service.models.order.OrderLineRequest;
import com.diploma.order_service.models.order.OrderLineResponse;
import com.diploma.order_service.models.product.BuyRequest;
import com.diploma.order_service.repository.OrderLineRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;

    // public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
    //     var order = orderLineMapper.toOrderLine(orderLineRequest);
    //     return orderLineRepository.save(order).getId();
    // }

    // public List<OrderLineResponse> findOrderLineById(Integer orderId) {
    //     return orderLineRepository.findAllByOrderId(orderId).stream().map(orderLineMapper::toOrderLineResponse).collect(Collectors.toList());
    // }

    public Mono<Integer> saveOrderLine(OrderLineRequest orderLineRequest) {
        return orderLineRepository.save(orderLineMapper.toOrderLine(orderLineRequest)).map(OrderLine::getId);
    }

    public Flux<OrderLineResponse> findOrderLineById(Integer orderId) {
        return orderLineRepository.findAllByOrderId(orderId)
                .map((OrderLine orderLine) -> orderLineMapper.toOrderLineResponse(orderLine));
    }

    public Flux<OrderLine> createOrderLines(Integer orderId, List<OrderLineRequest> requests) {
        return Flux.fromIterable(requests)
                .map(request -> {
                    OrderLine line = orderLineMapper.toOrderLine(request);
                    line.setOrderId(orderId);
                    return line;
                })
                .flatMap(orderLineRepository::save);
    }

    public Flux<OrderLine> createOrderLinesFromBuyRequests(Integer orderId, List<BuyRequest> requests) {
        return Flux.fromIterable(requests)
                .map((BuyRequest request) -> OrderLine.builder()
                        .orderId(orderId)
                        .productId(request.productId())
                        .quantity(BigDecimal.valueOf(request.requestedQuantity()))
                        .build())
                .flatMap(orderLineRepository::save);
    }

}
