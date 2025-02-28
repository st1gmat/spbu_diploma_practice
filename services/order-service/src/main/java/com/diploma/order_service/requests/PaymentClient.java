package com.diploma.order_service.requests;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.diploma.order_service.configs.FeignConfig;
import com.diploma.order_service.models.payment.PaymentRequest;

@FeignClient(name = "payment-service", url = "${application.config.payment-url}", configuration = FeignConfig.class)
public interface PaymentClient {
    
    @PostMapping
    Integer requestOrderPayment(@RequestBody PaymentRequest request);

}
