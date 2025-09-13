package com.hhnatsiuk.api_auth_service.impl.messaging_rabbit;

import com.hhnatsiuk.api_auth_adapter_db.repository.outbox.OutboxEventRepository;
import com.hhnatsiuk.api_auth_core.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxRepublisher {

    private static final Logger logger = LoggerFactory.getLogger(OutboxRepublisher.class);
    private static final int MAX_ATTEMPTS = 10;

    private final OutboxEventRepository outboxRepo;
    private final OutboxPublishTxService publishTxService;

    public OutboxRepublisher(OutboxEventRepository outboxRepo, OutboxPublishTxService publishTxService) {
        this.outboxRepo = outboxRepo;
        this.publishTxService = publishTxService;
    }

    @Scheduled(fixedDelayString = "${outbox.republisher.fixed-delay-ms:5000}")
    public void tick() {
        var toRepublish = outboxRepo
                .findTop100ByStatusInAndPublishAttemptsLessThanOrderByOccurredAtAsc(
                        List.of(OutboxStatus.NEW, OutboxStatus.FAILED), MAX_ATTEMPTS);

        if (toRepublish.isEmpty()) {
            return;
        }

        for (var e : toRepublish) {
            try {
                publishTxService.publishOne(e.getEventId());
            } catch (Exception ex) {
                logger.warn("Republish error for eventId={}: {}", e.getEventId(), ex.getMessage());
            }
        }
    }
}