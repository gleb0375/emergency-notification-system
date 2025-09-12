package com.hhnatsiuk.api_auth_service.impl.messaging_rabbit;

import com.hhnatsiuk.api_auth_adapter_db.repository.outbox.OutboxEventRepository;
import com.hhnatsiuk.api_auth_core.outbox.OutboxStatus;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OutboxPublishTxService {

    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange authEventsExchange;
    private final OutboxEventRepository outboxEventRepository;

    public OutboxPublishTxService(RabbitTemplate rabbitTemplate, TopicExchange authEventsExchange, OutboxEventRepository outboxEventRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.authEventsExchange = authEventsExchange;
        this.outboxEventRepository = outboxEventRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishOne(String eventId) {
        var e = outboxEventRepository.findByEventId(eventId).orElse(null);
        if (e == null || e.getStatus() == OutboxStatus.PUBLISHED) return;

        try {
            rabbitTemplate.convertAndSend(
                    authEventsExchange.getName(),
                    e.getEventType(),
                    e.getPayload(),
                    msg -> {
                        msg.getMessageProperties().setContentType("application/json");
                        msg.getMessageProperties().setMessageId(e.getEventId());
                        return msg;
                    }
            );
            e.setStatus(OutboxStatus.PUBLISHED);
            e.setPublishedAt(LocalDateTime.now());
        } catch (Exception ex) {
            e.setStatus(OutboxStatus.FAILED);
            e.setPublishAttempts(e.getPublishAttempts() + 1);
            e.setLastError(ex.getMessage());
        }

        outboxEventRepository.save(e);
    }
}
