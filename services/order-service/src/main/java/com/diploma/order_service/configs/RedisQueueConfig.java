package com.diploma.order_service.configs;

import com.diploma.order_service.models.product.BuyRequestWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisQueueConfig {

    @Bean
    public ReactiveRedisTemplate<String, BuyRequestWrapper> buyRequestQueueTemplate(
            ReactiveRedisConnectionFactory factory
    ) {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new AfterburnerModule());

        RedisSerializer<BuyRequestWrapper> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, BuyRequestWrapper.class);

        RedisSerializationContext<String, BuyRequestWrapper> context = RedisSerializationContext
                .<String, BuyRequestWrapper>newSerializationContext(RedisSerializer.string())
                .value(serializer)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
