package com.diploma.order_service.requests;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.diploma.order_service.models.customer.CustomerResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerClient {
    private final RestTemplate restTemplate;

    @Value("${application.config.customer-url}")
    private String customerServiceUrl;

    // @Retryable(
    //     value = { Exception.class },
    //     maxAttempts = 3,
    //     backoff = @Backoff(delay = 1000)
    // )
    public Optional<CustomerResponse> findById(String customerId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<CustomerResponse> response = restTemplate.exchange(
                    customerServiceUrl + "/" + customerId,
                    HttpMethod.GET,
                    requestEntity,
                    CustomerResponse.class
            );

            return Optional.ofNullable(response.getBody());

        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching customer by ID", e);
        }
    }

    @Recover
    public Optional<CustomerResponse> recover(Exception e, String customerId) {
        System.out.println("[Fallback] Failed to fetch customer after retries for ID: " + customerId);
        return Optional.empty();
    }
}
