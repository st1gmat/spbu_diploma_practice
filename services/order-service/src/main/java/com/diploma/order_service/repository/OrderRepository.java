package com.diploma.order_service.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.diploma.order_service.models.order.Order;

public interface OrderRepository extends ReactiveCrudRepository<Order, Integer> {
    
}
