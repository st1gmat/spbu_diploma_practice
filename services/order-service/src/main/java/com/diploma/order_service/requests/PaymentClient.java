package com.diploma.order_service.requests;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.diploma.order_service.models.payment.PaymentRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentClient {
    private final RestTemplate restTemplate;

    @Value("${application.config.payment-url}")
    private String paymentServiceUrl;

    public void requestOrderPayment(PaymentRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PaymentRequest> requestEntity = new HttpEntity<>(request, headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    paymentServiceUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Void.class
            );

            if (response.getStatusCode().isError()) {
                throw new RuntimeException("Error from payment service: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Payment request failed", e);
        }
    }
}
