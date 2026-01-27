package com.smartmatch.match_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "match.events.exchange";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String STREAM_QUEUE = "stream.queue";

    public static final String ROUTING_KEY_PATTERN = "match.#";
    public static final String ROUTING_KEY = "match.event";

    @Bean
    public TopicExchange matchExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE);
    }

    @Bean
    public Queue streamQueue() {
        return new Queue(STREAM_QUEUE);
    }

    @Bean
    public Binding notificationBinding(
            @Qualifier("notificationQueue") Queue queue,
            TopicExchange matchExchange) {
        return BindingBuilder.bind(queue).to(matchExchange).with(ROUTING_KEY_PATTERN);
    }

    @Bean
    public Binding streamBinding(
            @Qualifier("streamQueue") Queue queue,
            TopicExchange matchExchange) {
        return BindingBuilder.bind(queue).to(matchExchange).with(ROUTING_KEY_PATTERN);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
