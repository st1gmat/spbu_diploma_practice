package com.diploma.order_service.requests;

import com.diploma.order_service.models.product.BuyRequest;
import com.diploma.order_service.models.product.BuyResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
public class ProductClient {

    private final WebClient webClient;
    private final String productUrl;

    public ProductClient(@Qualifier("productWebClient") WebClient webClient,
                         @Value("${application.config.product-url}") String productUrl) {
        this.webClient = webClient;
        this.productUrl = productUrl;
    }

    public Mono<List<BuyResponse>> buy(List<BuyRequest> requests) {
        return Mono.defer(() -> {
            System.out.println("[ProductClient] Performing product buy request...");
            return webClient.post()
                    .uri(productUrl + "/buy")
                    .bodyValue(requests)
                    .retrieve()
                    .bodyToFlux(BuyResponse.class)
                    .collectList();
        }).retryWhen(
            Retry.backoff(5, Duration.ofSeconds(1))
                .maxBackoff(Duration.ofSeconds(3))
                .filter(throwable -> {
                    System.out.println("[Retry] caught: " + throwable.getClass().getSimpleName());
                    return throwable instanceof org.springframework.web.reactive.function.client.WebClientRequestException;
                })
                .onRetryExhaustedThrow((retrySpec, signal) -> signal.failure())
        );
    }
}