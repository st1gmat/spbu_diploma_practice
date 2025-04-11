package com.diploma.order_service.configs;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
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

    @Bean
    public WebClient customerWebClient() {
        return createWebClient("customer-pool");
    }

    @Bean
    public WebClient productWebClient() {
        return createWebClient("product-pool");
    }

    @Bean
    public WebClient paymentWebClient() {
        return createWebClient("payment-pool");
    }

    private WebClient createWebClient(String poolName) {
        ConnectionProvider connectionProvider = ConnectionProvider.builder(poolName)
            .maxConnections(250)                      // сбалансировано: не слишком много
            .pendingAcquireMaxCount(1000)             // сколько потоков может ждать свободное соединение
            .pendingAcquireTimeout(Duration.ofSeconds(20)) // ждем соединение максимум 20 секунд
            .evictInBackground(Duration.ofSeconds(30))     // чистим соединения, которые простаивают
            .maxIdleTime(Duration.ofSeconds(20))           // соединения, простаивающие дольше, убиваются
            .maxLifeTime(Duration.ofMinutes(5))            // убираем старые соединения
            .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .responseTimeout(Duration.ofSeconds(5))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(5))
                        .addHandlerLast(new WriteTimeoutHandler(5)))
                .compress(true);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // Добавляем фильтр, который при ошибке повторяет запрос 3 раза с задержкой 100 мс.
                .filter((request, next) ->
                        next.exchange(request)
                            .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100))))
                .build();
    }
}
