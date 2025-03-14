package com.diploma.customer_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diploma.customer_service.models.CustomerRequest;
import com.diploma.customer_service.models.CustomerResponse;
import com.diploma.customer_service.services.CustomerService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @PostMapping("/create")
    public Mono<String> createCustomer(@RequestBody CustomerRequest request) {
        return service.createCustomer(request);
    }

    @PutMapping
    public Mono<Void> updateCustomer(@RequestBody CustomerRequest request) {
        return service.updateCustomer(request);
    }

    @GetMapping
    public Flux<CustomerResponse> findAll() {
        return service.findAllCustomers();
    }

    @GetMapping("/{customer_id}")
    public Mono<CustomerResponse> findById(@PathVariable("customer_id") String customerId) {
        return service.findCustomersById(customerId);
    }
    
}