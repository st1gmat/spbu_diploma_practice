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
import reactor.util.retry.Retry;

import java.time.Duration;

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
        return createWebClient("customer-pool", customerUrl, 200, 300);
    }

    @Bean
    public WebClient productWebClient() {
        return createWebClient("product-pool", productUrl, 500, 1000);
    }

    @Bean
    public WebClient paymentWebClient() {
        return createWebClient("payment-pool", paymentUrl, 500, 1000);
    }

    private WebClient createWebClient(String poolName, String baseUrl, int maxConnections, int pendingAcquireMax) {

        ConnectionProvider connectionProvider = ConnectionProvider.builder(poolName)
            .maxConnections(maxConnections)                        // сколько соединений держим открытыми
            .pendingAcquireMaxCount(pendingAcquireMax)             // сколько могут ждать свободное соединение
            .pendingAcquireTimeout(Duration.ofSeconds(10))         // ждем максимум 10 секунд
            .maxIdleTime(Duration.ofSeconds(15))                   // соединения, простаивающие дольше — убиваются
            .maxLifeTime(Duration.ofMinutes(5))                    // удаляем старые соединения
            .evictInBackground(Duration.ofSeconds(30))            // чистим в фоне
            .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)   // 2 секунды на подключение
            .responseTimeout(Duration.ofSeconds(5))               // 5 секунд максимум на ответ
            .doOnConnected(conn -> conn
                .addHandlerLast(new ReadTimeoutHandler(5))        // 5 сек таймаут чтения
                .addHandlerLast(new WriteTimeoutHandler(5)))      // 5 сек таймаут записи
            .compress(true);

        return WebClient.builder()
            .baseUrl(baseUrl)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            // .filter((request, next) ->
            //     next.exchange(request)
            //         .retryWhen(Retry.fixedDelay(2, Duration.ofMillis(300))
            //             .filter(ex -> ex instanceof java.io.IOException)))  // retry только на IO
            .build();
    }
}
