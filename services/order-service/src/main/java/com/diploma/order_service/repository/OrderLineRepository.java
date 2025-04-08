package com.diploma.order_service.repository;

import com.diploma.order_service.models.order.OrderLine;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderLineRepository extends ReactiveCrudRepository<OrderLine, Integer> {
    Flux<OrderLine> findAllByOrderId(Integer orderId);
}