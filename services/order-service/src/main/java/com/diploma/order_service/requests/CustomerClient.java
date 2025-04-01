package com.diploma.order_service.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.diploma.order_service.models.customer.CustomerResponse;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerClient {

    private final WebClient webClient;

    @Value("${application.config.customer-url}")
    private String customerUrl;

    public Mono<Boolean> existsById(String customerId) {
        return webClient.get()
                .uri(customerUrl + "/" + customerId)
                .retrieve()
                .bodyToMono(CustomerResponse.class) // or CustomerResponse.class if needed
                .map(response -> true)
                .onErrorResume(e -> Mono.just(false));
    }

    public Mono<CustomerResponse> findById(String customerId) {
        return webClient.get()
                .uri(customerUrl + "/" + customerId)
                .retrieve()
                .bodyToMono(CustomerResponse.class) // or CustomerResponse.class if needed
                .onErrorResume(e -> Mono.just(null));
    }
}