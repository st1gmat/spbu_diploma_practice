package com.diploma.customer_service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.diploma.customer_service.models.Customer;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String>{

    
}