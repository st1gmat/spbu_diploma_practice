package com.diploma.payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diploma.payment_service.models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    
}
