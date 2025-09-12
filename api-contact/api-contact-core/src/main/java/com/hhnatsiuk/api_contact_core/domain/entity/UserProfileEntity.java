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
@Table(name = "user_profiles")
public class UserProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, length = 36, unique = true)
    private String uuid; // own profile UUID

    @Column(name = "account_uuid", nullable = false, length = 36, unique = true)
    private String accountUuid; // soft-link to auth_db.auth_accounts.uuid

    @Column(name = "surname", length = 100)
    private String surname;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "telegram_username", length = 32, unique = true)
    private String telegramUsername;

    @Column(name = "phone_number", length = 20, unique = true)
    private String phoneNumber;

    @Column(name = "preferred_channel", length = 9)
    private String preferredChannel;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (preferredChannel == null) {
            preferredChannel = "email";
        }
    }
}
