package com.diploma.customer_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.diploma.customer_service.models.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String>{

}
