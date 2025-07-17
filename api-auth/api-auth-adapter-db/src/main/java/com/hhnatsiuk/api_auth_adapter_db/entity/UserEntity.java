package com.hhnatsiuk.api_auth_adapter_db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credentials_id", nullable = false, unique = true)
    private CredentialEntity credential;

    @Column(name = "telegram_username", length = 100)
    private String telegramUsername;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }
    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ———————————————— Business method ————————————————
    public void updateContact(String telegram, String phone) {
        this.telegramUsername = telegram;
        this.phoneNumber = phone;
    }
}
