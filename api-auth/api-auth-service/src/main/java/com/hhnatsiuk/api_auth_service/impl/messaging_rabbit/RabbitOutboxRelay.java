package com.hhnatsiuk.api_auth_service.impl.messaging_rabbit;

import com.hhnatsiuk.api_auth_core.outbox.OutboxSavedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Service
public class RabbitOutboxRelay {

    private final OutboxPublishTxService publishTxService;

    public RabbitOutboxRelay(OutboxPublishTxService publishTxService) {
        this.publishTxService = publishTxService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOutBoxSaved(OutboxSavedEvent event) {
        publishTxService.publishOne(event.eventId());
    }
}
