package com.diploma.order_service.requests;

import com.diploma.order_service.models.product.BuyRequest;
import com.diploma.order_service.models.product.BuyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductClient {

    private final WebClient webClient;

    @Value("${application.config.product-url}")
    private String productUrl;

    public Mono<List<BuyResponse>> buy(List<BuyRequest> requests) {
        return webClient.post()
                .uri(productUrl + "/buy")
                .bodyValue(requests)
                .retrieve()
                .bodyToFlux(BuyResponse.class)
                .collectList();
    }
}
