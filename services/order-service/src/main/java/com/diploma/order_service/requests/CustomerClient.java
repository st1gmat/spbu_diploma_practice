package com.diploma.order_service.requests;

import lombok.RequiredArgsConstructor;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.diploma.order_service.models.customer.CustomerResponse;

import reactor.core.publisher.Mono;

@Service
// @RequiredArgsConstructor
public class CustomerClient {

    private final WebClient webClient;
    private final String customerUrl;
    private final ReactiveRedisTemplate<String, CustomerResponse> customerRedisTemplate;

    private static final Duration CACHE_TTL = Duration.ofMinutes(10);

    public CustomerClient(
        @Qualifier("customerWebClient") WebClient webClient,
        @Value("${application.config.customer-url}") String customerUrl,
        ReactiveRedisTemplate<String, CustomerResponse> customerRedisTemplate
    ) {
        this.webClient = webClient;
        this.customerUrl = customerUrl;
        this.customerRedisTemplate = customerRedisTemplate;
    }

    public Mono<Boolean> existsById(String customerId) {
        return findById(customerId)
                .map(response -> true)
                .switchIfEmpty(Mono.just(false));
    }

    public Mono<CustomerResponse> findById(String customerId) {
        String cacheKey = "customer:" + customerId;

        return customerRedisTemplate.opsForValue().get(cacheKey)
            .switchIfEmpty(
                webClient.get()
                    .uri(customerUrl + "/" + customerId)
                    .retrieve()
                    .bodyToMono(CustomerResponse.class)
                    .flatMap(response ->
                        customerRedisTemplate.opsForValue()
                            .set(cacheKey, response, CACHE_TTL)
                            .thenReturn(response)
                    )
            )
            .onErrorResume(e -> Mono.empty()); // Fallback на ошибку
    }
}

