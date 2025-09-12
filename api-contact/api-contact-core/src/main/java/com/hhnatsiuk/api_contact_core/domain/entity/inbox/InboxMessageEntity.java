package com.hhnatsiuk.api_contact_core.domain.entity.inbox;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "message_log",
        uniqueConstraints = @UniqueConstraint(name = "uq_message_id", columnNames = "message_id")
)
public class InboxMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_id", nullable = false, length = 100)
    private String messageId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "occurred_at")
    private LocalDateTime occurredAt;

    @Column(name = "received_at", insertable = false, updatable = false)
    private LocalDateTime receivedAt; // DEFAULT CURRENT_TIMESTAMP (БД)

    @Column(name = "payload_hash", length = 64)
    private String payloadHash; // SHA-256 hex при желании

    @Convert(converter = InboxStatusConverter.class)
    @Column(name = "status", nullable = false, length = 9) // 'processed' = 9
    private InboxStatus status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @PrePersist
    public void prePersist() {
        if (status == null) status = InboxStatus.PROCESSED;
    }

    public void markProcessed() {
        this.status = InboxStatus.PROCESSED;
        this.errorMessage = null;
    }

    public void markFailed(String error) {
        this.status = InboxStatus.FAILED;
        this.errorMessage = error;
    }
}
