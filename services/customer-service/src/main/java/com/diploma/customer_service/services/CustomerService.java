package com.diploma.customer_service.services;

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
        return repository.save(mapper.toCustomer(request)).map(Customer::getId);
    }

    public Mono<Void> updateCustomer(CustomerRequest request) {
        return repository.findById(request.id())
                .switchIfEmpty(
                        Mono.error(
                                new CustomerNotFoundException(
                                        "updateCustomer:: customer update failed: id not found: " + request.id())))
                .flatMap(customer -> {
                    mergeCustomer(customer, request);
                    return repository.save(customer);
                })
                .then();
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstName())) {
            customer.setFirstName(request.firstName());
        }
        if (StringUtils.isNotBlank(request.lastName())) {
            customer.setLastName(request.lastName());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    public Flux<CustomerResponse> findAllCustomers() {
        return repository.findAll().map(mapper::fromCustomer);
    }

    public Mono<CustomerResponse> findCustomersById(String customerId) {
        return repository.findById(customerId)
                .map(mapper::fromCustomer)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(
                        "Customer not found with ID: " + customerId)));
    }

}