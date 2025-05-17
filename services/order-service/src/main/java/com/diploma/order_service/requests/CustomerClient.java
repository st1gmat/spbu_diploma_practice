package com.diploma.order_service.requests;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
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

    @Cacheable(value = "customers", key = "#customerId", unless = "#result == null")
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

    public Optional<CustomerResponse> fallbackFindById(String customerId, Throwable throwable) {
        // Можно логировать причину падения
        System.err.println("Fallback triggered for customerId=" + customerId + ": " + throwable.getMessage());

        // Возвращаем пустой Optional (или можно подставить дефолтное значение)
        return Optional.empty();
    }
}
