package com.diploma.order_service.requests;

import com.diploma.order_service.models.payment.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentClient {

    private final WebClient webClient;

    @Value("${application.config.payment-url}")
    private String paymentUrl;

    public Mono<Void> pay(PaymentRequest request) {
        return webClient.post()
                .uri(paymentUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }
}