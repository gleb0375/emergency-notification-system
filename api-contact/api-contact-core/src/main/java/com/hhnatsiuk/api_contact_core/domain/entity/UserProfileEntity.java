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

    @Column(name = "user_profile_uuid", nullable = false, length = 36, unique = true, updatable = false)
    private String userProfileUuid;

    @Column(name = "auth_account_uuid", nullable = false, length = 36, unique = true, updatable = false)
    private String authAccountUuid;

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

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country_code", length = 2)
    private String countryCode; // e.g. "CZ"

    @Column(name = "preferred_channel", nullable = false, length = 9)
    private String preferredChannel;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void applyDefaults() {
        if (preferredChannel == null) {
            preferredChannel = "email";
        }
        if (countryCode != null) {
            countryCode = countryCode.toUpperCase();
        }
    }
}

