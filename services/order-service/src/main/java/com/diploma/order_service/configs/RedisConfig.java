package com.diploma.order_service.configs;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.diploma.order_service.models.customer.CustomerResponse;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, CustomerResponse> customerRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, CustomerResponse> context = RedisSerializationContext
            .<String, CustomerResponse>newSerializationContext(new StringRedisSerializer())
            .value(new Jackson2JsonRedisSerializer<>(CustomerResponse.class))
            .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}

