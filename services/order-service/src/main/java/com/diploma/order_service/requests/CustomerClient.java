package com.diploma.order_service.requests;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.diploma.order_service.configs.FeignConfig;
import com.diploma.order_service.models.customer.CustomerResponse;

@FeignClient(name = "customer-service", url = "${application.config.customer-url}", configuration = FeignConfig.class)
public interface CustomerClient {

    @GetMapping("/{customer_id}")
    public Optional<CustomerResponse> findById(@PathVariable("customer_id") String customerId);
}