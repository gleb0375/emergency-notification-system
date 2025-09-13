package com.hhnatsiuk.api_auth_adapter_db.repository.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhnatsiuk.api_auth_core.outbox.OutboxAppender;
import com.hhnatsiuk.api_auth_core.outbox.OutboxEventEntity;
import com.hhnatsiuk.api_auth_core.outbox.OutboxSavedEvent;
import com.hhnatsiuk.api_auth_core.outbox.OutboxStatus;
import com.hhnatsiuk.api_auth_if.model.generated.UserCreatedEventPayloadDTO;
import com.hhnatsiuk.api_auth_if.model.generated.UserDeletedEventPayloadDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OutboxAppenderJpa implements OutboxAppender {

    private static final Logger logger = LoggerFactory.getLogger(OutboxAppenderJpa.class);

    private static final String AGGREGATE_TYPE_AUTH_ACCOUNT = "auth_account";
    private static final String USER_CREATED = "user.user_created";
    private static final String USER_DELETED = "user.user_deleted";

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher appEventPublisher;

    public OutboxAppenderJpa(OutboxEventRepository outboxEventRepository,
                             ObjectMapper objectMapper,
                             ApplicationEventPublisher appEventPublisher) {
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
        this.appEventPublisher = appEventPublisher;
    }

    @Transactional
    @Override
    public void appendUserCreated(String accountUuid, String email) {
        logger.debug("Preparing outbox event: {} for accountUuid={}, email={}",
                USER_CREATED, accountUuid, email);

        var payloadObj = new UserCreatedEventPayloadDTO()
                .accountUuid(accountUuid)
                .email(email);

        String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(payloadObj);
            logger.debug("Serialized payload: {}", payloadJson);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize EmailVerifiedEventPayloadDTO for accountUuid={}", accountUuid, e);
            throw new RuntimeException("Failed to serialize EmailVerifiedEventPayloadDTO", e);
        }

        var event = OutboxEventEntity.builder()
                .eventId(UUID.randomUUID().toString())
                .aggregateType(AGGREGATE_TYPE_AUTH_ACCOUNT)
                .aggregateId(accountUuid)
                .eventType(USER_CREATED)
                .payload(payloadJson)
                .status(OutboxStatus.NEW)
                .occurredAt(LocalDateTime.now())
                .build();

        outboxEventRepository.save(event);
        logger.info("Outbox event persisted: {} for accountUuid={}", USER_CREATED, accountUuid);

        appEventPublisher.publishEvent(new OutboxSavedEvent(event.getEventId()));
    }

    @Transactional
    @Override
    public void appendUserDeleted(String accountUuid) {
        logger.debug("Preparing outbox event: {} for accountUuid={}", USER_DELETED, accountUuid);

        var payloadObj = new UserDeletedEventPayloadDTO()
                .accountUuid(accountUuid);

        String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(payloadObj);
            logger.debug("Serialized payload: {}", payloadJson);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize UserDeletedEventPayloadDTO for accountUuid={}", accountUuid, e);
            throw new RuntimeException("Failed to serialize UserDeletedEventPayloadDTO", e);
        }

        var event = OutboxEventEntity.builder()
                .eventId(UUID.randomUUID().toString())
                .aggregateType(AGGREGATE_TYPE_AUTH_ACCOUNT)
                .aggregateId(accountUuid)
                .eventType(USER_DELETED)
                .payload(payloadJson)
                .status(OutboxStatus.NEW)
                .occurredAt(LocalDateTime.now())
                .build();

        outboxEventRepository.save(event);
        logger.info("Outbox event persisted: {} for accountUuid={}", USER_DELETED, accountUuid);

        appEventPublisher.publishEvent(new OutboxSavedEvent(event.getEventId()));
    }
}
