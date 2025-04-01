package com.diploma.order_service.services;

import com.diploma.order_service.models.customer.CustomerResponse;
import com.diploma.order_service.models.payment.PaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.Optional;

@Service
public class ExternalServiceClient {

    private final RestTemplate restTemplate;

    @Value("${application.config.customer-url}")
    private String customerServiceUrl;

    @Value("${application.config.payment-url}")
    private String paymentServiceUrl;

    public ExternalServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<CustomerResponse> findCustomerById(String customerId) {
        String url = customerServiceUrl + "/" + customerId;
        try {
            ResponseEntity<CustomerResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    CustomerResponse.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Integer requestOrderPayment(PaymentRequest request) {
        HttpEntity<PaymentRequest> entity = new HttpEntity<>(request);
        ResponseEntity<Integer> response = restTemplate.exchange(
                paymentServiceUrl,
                HttpMethod.POST,
                entity,
                Integer.class
        );
        return response.getBody();
    }
}
