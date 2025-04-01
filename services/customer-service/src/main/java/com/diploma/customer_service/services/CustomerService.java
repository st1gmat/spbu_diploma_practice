package com.diploma.customer_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.diploma.customer_service.excpetions.CustomerNotFoundException;
import com.diploma.customer_service.models.Customer;
import com.diploma.customer_service.models.CustomerRequest;
import com.diploma.customer_service.models.CustomerResponse;
import com.diploma.customer_service.repository.CustomerRepository;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerRequest request) {
        var customer = repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        
        var customer = repository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                    String.format("Customer update goes wrong: \n \t No customers found with ID: %s", request.id())
                ));
        mergeCustomer(customer, request);
        repository.save(customer);

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

    public List<CustomerResponse> findAllCustomers() {
        return repository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public CustomerResponse findCustomersById(String customerId) {
        return repository.findById(customerId)
                .map(mapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(
                    String.format("Customer update goes wrong: \n \t No customers found with ID: %s", customerId)
                ));
    }

}