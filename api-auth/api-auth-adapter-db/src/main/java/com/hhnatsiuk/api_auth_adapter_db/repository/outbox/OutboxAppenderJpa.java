package com.hhnatsiuk.api_auth_adapter_db.repository.outbox;

import com.hhnatsiuk.api_auth_core.outbox.OutboxAppender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutboxAppenderJpa implements OutboxAppender {

    public OutboxAppenderJpa(OutboxEventRepository outboxEventRepository) {
        this.outboxEventRepository = outboxEventRepository;
    }

    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    @Override
    public void appendEmailVerified(String accountUuid, String email) { /* save NEW event */ }

    @Transactional
    @Override
    public void appendUserDeleted(String accountUuid) { /* save NEW event */ }
}
