package com.diploma.customer_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.diploma.customer_service.exceptions.CustomerNotFoundException;
import com.diploma.customer_service.models.Customer;
import com.diploma.customer_service.models.CustomerRequest;
import com.diploma.customer_service.models.CustomerResponse;
import com.diploma.customer_service.repository.CustomerRepository;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public Mono<String> createCustomer(CustomerRequest request) {
        // var customer = repository.save(mapper.toCustomer(request));
        // return customer.getId();
        return repository.save(mapper.toCustomer(request)).map(Customer::getId);
    }

    public Mono<Void> updateCustomer(CustomerRequest request) {
        
        // var customer = repository.findById(request.id())
        //         .orElseThrow(() -> new CustomerNotFoundException(
        //             String.format("Customer update goes wrong: \n \t No customers found with ID: %s", request.id())
        //         ));
        // mergeCustomer(customer, request);
        // repository.save(customer);
        return repository.findById(request.id())
            .switchIfEmpty(
                Mono.error(
                    new CustomerNotFoundException("updateCustomer:: customer update failed: id not found: " + request.id()
                )))
            .flatMap(customer -> {
                mergeCustomer(customer, request);
                return repository.save(customer);
            })
            .then();

    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstName())) {
            customer.setFirstName(request.firstName());
        }
        if(StringUtils.isNotBlank(request.lastName())) {
            customer.setLastName(request.lastName());
        }
        if(StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if(request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    public Flux<CustomerResponse> findAllCustomers() {
        // return repository.findAll()
        //         .stream()
        //         .map(mapper::fromCustomer)
        //         .collect(Collectors.toList());
        return repository.findAll().map(mapper::fromCustomer);
    }

    public Mono<CustomerResponse> findCustomersById(String customerId) {
        return repository.findById(customerId)
                .map(mapper::fromCustomer)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(
                    "Customer not found with ID: " + customerId
                )));
    }

}