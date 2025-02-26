package com.diploma.order_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diploma.order_service.models.order.OrderLine;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer>{
    List<OrderLine> findAllByOrderId(Integer orderId);
}
