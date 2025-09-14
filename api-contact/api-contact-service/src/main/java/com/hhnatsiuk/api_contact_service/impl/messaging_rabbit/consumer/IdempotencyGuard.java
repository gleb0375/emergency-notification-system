package com.hhnatsiuk.api_contact_service.impl.messaging_rabbit.consumer;

import com.hhnatsiuk.api_contact_adapter_db.repository.inbox.InboxMessageRepository;
import com.hhnatsiuk.api_contact_core.domain.entity.inbox.InboxMessageEntity;
import com.hhnatsiuk.api_contact_core.domain.entity.inbox.InboxStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;

@Service
public class IdempotencyGuard {

    private static final Logger logger = LoggerFactory.getLogger(IdempotencyGuard.class);

    private final InboxMessageRepository inboxRepo;

    public IdempotencyGuard(InboxMessageRepository inboxRepo) {
        this.inboxRepo = inboxRepo;
    }

    @Transactional
    public void process(String messageId,
                        String eventType,
                        LocalDateTime occurredAt,
                        String payloadJson,
                        Runnable action) {

        if (messageId == null || messageId.isBlank()) {
            logger.warn("Processing message without messageId; eventType={}", eventType);
        } else if (inboxRepo.existsByMessageId(messageId)) {
            logger.debug("Message {} already processed, skipping", messageId);
            return;
        }

        String payloadHash = sha256HexSafe(payloadJson);

        try {
            action.run();

            InboxMessageEntity ok = InboxMessageEntity.builder()
                    .messageId(messageId)
                    .eventType(eventType)
                    .occurredAt(occurredAt)
                    .payloadHash(payloadHash)
                    .status(InboxStatus.PROCESSED)
                    .build();
            inboxRepo.save(ok);

        } catch (Exception ex) {
            try {
                InboxMessageEntity failed = InboxMessageEntity.builder()
                        .messageId(messageId)
                        .eventType(eventType)
                        .occurredAt(occurredAt)
                        .payloadHash(payloadHash)
                        .status(InboxStatus.FAILED)
                        .errorMessage(crop(ex.getMessage(), 4000))
                        .build();
                inboxRepo.save(failed);
            } catch (DataIntegrityViolationException ignore) {
            }
            throw ex;
        }
    }

    private String sha256HexSafe(String json) {
        if (json == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(json.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private String crop(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
}
