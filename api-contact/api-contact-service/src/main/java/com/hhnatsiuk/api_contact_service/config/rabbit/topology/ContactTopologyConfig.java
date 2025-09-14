package com.hhnatsiuk.api_contact_service.config.rabbit.topology;

import com.hhnatsiuk.api_contact_service.config.rabbit.properties.MessagingProperties;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MessagingProperties.class)
public class ContactTopologyConfig {

    // ---------- Exchanges ----------
    @Bean("authEventsExchange")
    public TopicExchange authEventsExchange(MessagingProperties props) {
        return ExchangeBuilder.topicExchange(props.getAuth().getExchange())
                .durable(true)
                .build();
    }

    @Bean("contactDlx")
    public TopicExchange contactDlx(MessagingProperties props) {
        return ExchangeBuilder.topicExchange(props.getContact().getDlx())
                .durable(true)
                .build();
    }

    // ---------- Queues ----------
    @Bean("contactQueue")
    public Queue contactQueue(MessagingProperties props) {
        return QueueBuilder.durable(props.getContact().getQueue())
                .withArgument("x-dead-letter-exchange", props.getContact().getDlx())
                .withArgument("x-dead-letter-routing-key", props.getContact().getDlq())
                .build();
    }

    @Bean("contactDlq")
    public Queue contactDlq(MessagingProperties props) {
        return QueueBuilder.durable(props.getContact().getDlq()).build();
    }

    // ---------- Bindings ----------
    @Bean
    public Binding bindUserCreated(@Qualifier("contactQueue") Queue contactQueue,
                                   @Qualifier("authEventsExchange") TopicExchange authEventsExchange,
                                   MessagingProperties props) {
        return BindingBuilder.bind(contactQueue)
                .to(authEventsExchange)
                .with(props.getAuth().getRouting().getUserCreated());
    }

    @Bean
    public Binding bindUserDeleted(@Qualifier("contactQueue") Queue contactQueue,
                                   @Qualifier("authEventsExchange") TopicExchange authEventsExchange,
                                   MessagingProperties props) {
        return BindingBuilder.bind(contactQueue)
                .to(authEventsExchange)
                .with(props.getAuth().getRouting().getUserDeleted());
    }

    @Bean
    public Binding bindDlq(@Qualifier("contactDlq") Queue contactDlq,
                           @Qualifier("contactDlx") TopicExchange contactDlx,
                           MessagingProperties props) {
        return BindingBuilder.bind(contactDlq)
                .to(contactDlx)
                .with(props.getContact().getDlq());
    }
}
