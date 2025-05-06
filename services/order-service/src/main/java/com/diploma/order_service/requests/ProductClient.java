package com.diploma.order_service.requests;

import com.diploma.order_service.models.product.BuyRequest;
import com.diploma.order_service.models.product.BuyResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

// @Service
// public class ProductClient {

//     private final WebClient webClient;
//     private final String productUrl;

//     public ProductClient(@Qualifier("productWebClient") WebClient webClient,
//                          @Value("${application.config.product-url}") String productUrl) {
//         this.webClient = webClient;
//         this.productUrl = productUrl;
//     }

//     public Mono<List<BuyResponse>> buy(List<BuyRequest> requests) {
//         return webClient.post()
//                 .uri(productUrl + "/buy")
//                 .bodyValue(requests)
//                 .retrieve()
//                 .bodyToFlux(BuyResponse.class)
//                 .collectList();
//     }
// }
@Service
public class ProductClient {

    private final WebClient webClient;
    private final String productUrl;
    private final ReactiveRedisTemplate<String, List<BuyResponse>> redisTemplate;

    public ProductClient(@Qualifier("productWebClient") WebClient webClient,
                         @Value("${application.config.product-url}") String productUrl,
                         ReactiveRedisTemplate<String, List<BuyResponse>> redisTemplate) {
        this.webClient = webClient;
        this.productUrl = productUrl;
        this.redisTemplate = redisTemplate;
    }

    public Mono<List<BuyResponse>> buy(List<BuyRequest> requests) {
        String cacheKey = generateCacheKey(requests);

        return redisTemplate.opsForValue().get(cacheKey)
                .switchIfEmpty(
                        webClient.post()
                            .uri(productUrl + "/buy")
                            .bodyValue(requests)
                            .retrieve()
                            .bodyToFlux(BuyResponse.class)
                            .collectList()
                            .flatMap(result ->
                                redisTemplate.opsForValue()
                                    .set(cacheKey, result, Duration.ofMinutes(10))
                                    .thenReturn(result)
                            )
                );
    }

    private String generateCacheKey(List<BuyRequest> requests) {
        return "product:buy:" + requests.hashCode(); // или JSON-хеш/MD5 по содержимому
    }
}

