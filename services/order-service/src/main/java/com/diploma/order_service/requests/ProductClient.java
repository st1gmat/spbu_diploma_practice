package com.diploma.order_service.requests;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.diploma.order_service.models.product.BuyRequest;
import com.diploma.order_service.models.product.BuyResponse;

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
        return webClient.post()
                .uri(productUrl + "/buy")
                .bodyValue(requests)
                .retrieve()
                .bodyToFlux(BuyResponse.class)
                .timeout(Duration.ofMillis(2500)) // таймаут ответа
                .retryWhen(
                        Retry.backoff(2, Duration.ofMillis(2000))  // 2 повтора, начальная задержка 2с
                             .jitter(0.0)                         // без случайных колебаний
                             .maxBackoff(Duration.ofMillis(4000)) // максимум 4с
                             .transientErrors(true)               // только на временные ошибки
                             .doBeforeRetry(retrySignal -> {
                                 System.out.println("Retrying due to: " + retrySignal.failure().getMessage());
                             })
                )
                .collectList()
                .onErrorResume(e -> Mono.error(new RuntimeException("Product service unavailable", e)));
    }
}
