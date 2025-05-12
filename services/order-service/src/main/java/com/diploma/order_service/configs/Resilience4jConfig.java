package com.diploma.order_service.configs;

import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
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
            .maxAttempts(2)
            .waitDuration(Duration.ofSeconds(1))
            .intervalFunction(
                io.github.resilience4j.core.IntervalFunction.ofExponentialBackoff(1000, 2.0, 2000)
            )
            .retryExceptions(
                java.io.IOException.class,
                java.net.SocketTimeoutException.class,
                org.springframework.web.client.ResourceAccessException.class
            )
            .ignoreExceptions(
                com.diploma.order_service.exceptions.BusinessException.class
            )
            .build();

        RetryRegistry registry = RetryRegistry.ofDefaults();
        registry.retry("productServiceRetry", retryConfig);
        return registry;
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig cbConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(40)
            .minimumNumberOfCalls(10)
            .slidingWindowSize(20)
            .waitDurationInOpenState(Duration.ofSeconds(6))
            .permittedNumberOfCallsInHalfOpenState(3)
            .automaticTransitionFromOpenToHalfOpenEnabled(true)
            .build();

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();
        registry.circuitBreaker("productServiceCB", cbConfig);
        return registry;
    }


    @Bean
    public BulkheadRegistry bulkheadRegistry() {
        BulkheadConfig bulkheadConfig = BulkheadConfig.custom()
            .maxConcurrentCalls(50)
            .maxWaitDuration(Duration.ZERO)
            .build();

        BulkheadRegistry registry = BulkheadRegistry.ofDefaults();
        registry.bulkhead("productServiceBulkhead", bulkheadConfig);
        return registry;
    }

}
