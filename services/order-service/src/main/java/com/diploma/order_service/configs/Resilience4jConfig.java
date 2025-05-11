package com.diploma.order_service.configs;

import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4jConfig {

    @Bean
    public RetryRegistry retryRegistry() {
        RetryConfig retryConfig = RetryConfig.custom()
            .maxAttempts(3) // 1 вызов + 2 повтора
            .waitDuration(Duration.ofSeconds(1)) // начальная задержка
            .retryExceptions(
                java.io.IOException.class,
                java.net.SocketTimeoutException.class,
                org.springframework.web.client.ResourceAccessException.class
            )
            .ignoreExceptions(
                com.diploma.order_service.exceptions.BusinessException.class
            )
            .intervalFunction(
                io.github.resilience4j.core.IntervalFunction.ofExponentialBackoff(1000, 2.0, 3000)
            )
            .build();

        return RetryRegistry.of(retryConfig);
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(50)
            .minimumNumberOfCalls(5)
            .slidingWindowSize(10)
            .waitDurationInOpenState(Duration.ofSeconds(6)) // <-- вот здесь
            .permittedNumberOfCallsInHalfOpenState(3)
            .automaticTransitionFromOpenToHalfOpenEnabled(true)
            .build();

        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }
}
