package com.hhnatsiuk.api_auth_service.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitProducerConfig {

    private final String AUTH_EVENTS_EXCHANGE = "ens.auth.events";

    @Bean
    public TopicExchange authEventsExchange() {
        return new TopicExchange(AUTH_EVENTS_EXCHANGE, true, false);
    }

}
