package com.hhnatsiuk.api_auth_adapter_db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification_tokens")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationTokenEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credentials_id", nullable = false)
    private CredentialEntity credential;

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }

    // ———————————————— Business method ————————————————
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
