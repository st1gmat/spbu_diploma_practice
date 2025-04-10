package com.diploma.order_service.requests;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.diploma.order_service.models.customer.CustomerResponse;

import reactor.core.publisher.Mono;

@Service
public class CustomerClient {

    private final WebClient webClient;
    private final String customerUrl;

    public CustomerClient(@Qualifier("customerWebClient") WebClient webClient,
                          @Value("${application.config.customer-url}") String customerUrl) {
        this.webClient = webClient;
        this.customerUrl = customerUrl;
    }

    public Mono<Boolean> existsById(String customerId) {
        return webClient.get()
                .uri(customerUrl + "/" + customerId)
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .map(response -> true)
                .onErrorResume(e -> Mono.just(false));
    }

    public Mono<CustomerResponse> findById(String customerId) {
        return webClient.get()
                .uri(customerUrl + "/" + customerId)
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .onErrorResume(e -> Mono.just(null));
    }
}
