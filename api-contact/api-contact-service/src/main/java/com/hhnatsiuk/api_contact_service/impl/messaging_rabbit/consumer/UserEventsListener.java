package com.hhnatsiuk.api_contact_service.impl.messaging_rabbit.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhnatsiuk.api_contact_adapter_db.repository.UserProfileRepository;
import com.hhnatsiuk.api_contact_core.domain.entity.UserProfileEntity;
import com.hhnatsiuk.api_contact_if.model.generated.UserCreatedEventPayloadDTO;
import com.hhnatsiuk.api_contact_if.model.generated.UserDeletedEventPayloadDTO;
import com.hhnatsiuk.api_contact_service.config.rabbit.properties.MessagingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class UserEventsListener {

    private static final Logger logger = LoggerFactory.getLogger(UserEventsListener.class);

    private final ObjectMapper objectMapper;
    private final IdempotencyGuard idempotency;
    private final UserProfileRepository userProfiles;
    private final MessagingProperties props;

    public UserEventsListener(ObjectMapper objectMapper,
                              IdempotencyGuard idempotency,
                              UserProfileRepository userProfiles,
                              MessagingProperties props) {
        this.objectMapper = objectMapper;
        this.idempotency = idempotency;
        this.userProfiles = userProfiles;
        this.props = props;
    }

    @RabbitListener(
            queues = "${ens.messaging.contact.queue}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void onMessage(Message message,
                          @Header(name = AmqpHeaders.RECEIVED_ROUTING_KEY, required = false) String routingKey,
                          @Header(name = AmqpHeaders.MESSAGE_ID, required = false) String messageId) {

        final String body = new String(message.getBody(), StandardCharsets.UTF_8);

        if (routingKey == null) {
            logger.warn("Received message without routingKey, messageId={}, body={}", messageId, body);
            throw new IllegalArgumentException("Missing routingKey");
        }

        if (routingKey.equals(props.getAuth().getRouting().getUserCreated())) {
            idempotency.process(messageId, routingKey, null, body, () -> handleUserCreated(body));
            return;
        }

        if (routingKey.equals(props.getAuth().getRouting().getUserDeleted())) {
            idempotency.process(messageId, routingKey, null, body, () -> handleUserDeleted(body));
            return;
        }

        logger.error("Unsupported routingKey={}, messageId={}, body={}", routingKey, messageId, body);
        throw new IllegalArgumentException("Unsupported routingKey: " + routingKey);
    }

    private void handleUserCreated(String json) {
        UserCreatedEventPayloadDTO dto = read(json, UserCreatedEventPayloadDTO.class);

        if (isBlank(dto.getAccountUuid())) throw new IllegalArgumentException("accountUuid is required");
        if (isBlank(dto.getEmail()))        throw new IllegalArgumentException("email is required");

        if (userProfiles.existsByAuthAccountUuid(dto.getAccountUuid())) {
            logger.debug("Profile already exists for accountUuid={}, skip", dto.getAccountUuid());
            return;
        }

        var entity = UserProfileEntity.builder()
                .userProfileUuid(UUID.randomUUID().toString())
                .authAccountUuid(dto.getAccountUuid())
                .email(dto.getEmail())
                .preferredChannel("email")
                .build();

        var saved = userProfiles.save(entity);
        logger.info("Created user_profile id={} for accountUuid={}", saved.getId(), dto.getAccountUuid());
    }

    private void handleUserDeleted(String json) {
        UserDeletedEventPayloadDTO dto = read(json, UserDeletedEventPayloadDTO.class);
        if (isBlank(dto.getAccountUuid())) throw new IllegalArgumentException("accountUuid is required");

        userProfiles.findByAuthAccountUuid(dto.getAccountUuid())
                .ifPresentOrElse(
                        e -> { userProfiles.delete(e); logger.info("Deleted user_profile for accountUuid={}", dto.getAccountUuid()); },
                        () -> logger.debug("No profile for accountUuid={}, nothing to delete", dto.getAccountUuid())
                );
    }

    private <T> T read(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON for " + type.getSimpleName(), e);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
