package com.hhnatsiuk.api_auth_adapter_db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
@Getter @Setter @NoArgsConstructor
public class RoleEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true, length = 255)
        private String email;

        @Column(nullable = false, length = 255)
        private String password;

        @Column(name = "is_email_verified", nullable = false)
        private boolean emailVerified;

        @Column(name = "last_login_at")
        private LocalDateTime lastLoginAt;

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @PrePersist
        void onCreate() { createdAt = LocalDateTime.now(); }

        @PreUpdate
        void onUpdate() { updatedAt = LocalDateTime.now(); }
}
