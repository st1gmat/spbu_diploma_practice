package com.diploma.order_service.configs;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${application.config.customer-url}")
    private String customerUrl;

    @Value("${application.config.product-url}")
    private String productUrl;

    @Value("${application.config.payment-url}")
    private String paymentUrl;

    @Bean
    public WebClient customerWebClient() {
        return createWebClient("customer-pool", customerUrl, 200, 1000);
    }

    @Bean
    public WebClient productWebClient() {
        return createWebClient("product-pool", productUrl, 300, 2000);
    }

    @Bean
    public WebClient paymentWebClient() {
        return createWebClient("payment-pool", paymentUrl, 300, 2000);
    }

    private WebClient createWebClient(String poolName, String baseUrl, int maxConnections, int pendingAcquireMax) {
        ConnectionProvider connectionProvider = ConnectionProvider.builder(poolName)
            .maxConnections(maxConnections)
            .pendingAcquireMaxCount(pendingAcquireMax)
            .pendingAcquireTimeout(Duration.ofSeconds(20))
            .maxIdleTime(Duration.ofSeconds(30))
            .maxLifeTime(Duration.ofMinutes(5))
            .evictInBackground(Duration.ofSeconds(60))
            .metrics(true)
            .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
            .responseTimeout(Duration.ofSeconds(10))
            .doOnConnected(conn -> conn
                .addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS)))
            .compress(true);

        return WebClient.builder()
            .baseUrl(baseUrl)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }
}
