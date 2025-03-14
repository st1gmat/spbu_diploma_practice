package com.diploma.payment_service.repository;


import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.diploma.payment_service.models.Payment;

public interface PaymentRepository extends R2dbcRepository<Payment, Integer> {
    
}