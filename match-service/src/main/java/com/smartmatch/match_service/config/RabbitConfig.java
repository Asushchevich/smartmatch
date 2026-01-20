package com.smartmatch.match_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "match.events.exchange";
    public static final String QUEUE = "notification.queue";
    public static final String ROUTING_KEY = "match.created";

    @Bean
    public TopicExchange matchExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE);
    }

    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange matchExchange) {
        return BindingBuilder.bind(notificationQueue).to(matchExchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
