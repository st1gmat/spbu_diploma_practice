package com.diploma.order_service.configs;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.diploma.order_service.models.product.BuyResponse;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, List<BuyResponse>> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<List<BuyResponse>> serializer =
                new Jackson2JsonRedisSerializer<>((Class<List<BuyResponse>>)(Class<?>)List.class);

        RedisSerializationContext<String, List<BuyResponse>> context = RedisSerializationContext
                .<String, List<BuyResponse>>newSerializationContext(RedisSerializer.string())
                .value(serializer)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
