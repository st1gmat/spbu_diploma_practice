package com.diploma.order_service.requests;

import com.diploma.order_service.models.payment.PaymentRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PaymentClient {

    private final WebClient webClient;
    private final String paymentUrl;

    public PaymentClient(@Qualifier("paymentWebClient") WebClient webClient,
                         @Value("${application.config.payment-url}") String paymentUrl) {
        this.webClient = webClient;
        this.paymentUrl = paymentUrl;
    }

    public Mono<Void> pay(PaymentRequest request) {
        return webClient.post()
                .uri(paymentUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
