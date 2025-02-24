package com.diploma.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diploma.order_service.models.order.OrderLine;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer>{

}
