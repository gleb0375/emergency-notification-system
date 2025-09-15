package com.hhnatsiuk.api_contact_core.domain.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification_templates")
public class NotificationTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "notification_template_uuid", nullable = false, length = 36, unique = true)
    private String notificationTemplateUuid;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "channel", nullable = false, length = 9)
    private String channel;

    @Column(name = "created_by_account_uuid", nullable = false, length = 36)
    private String createdByAccountUuid;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
