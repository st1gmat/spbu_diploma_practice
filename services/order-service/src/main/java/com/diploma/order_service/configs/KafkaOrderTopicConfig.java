package com.diploma.order_service.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaOrderTopicConfig {
    
    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder.name("order-topic").build();
    }

    @Bean
    public NewTopic buyRequestTopic() {
        return TopicBuilder.name("buy-request-topic").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic buyRequestDlqTopic() {
        return TopicBuilder.name("buy-request-dlq-topic").partitions(1).replicas(1).build();
    }
}
