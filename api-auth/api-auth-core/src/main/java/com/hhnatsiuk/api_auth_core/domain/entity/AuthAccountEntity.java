package com.hhnatsiuk.api_auth_core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "auth_accounts")
@Getter
@Setter
@NoArgsConstructor
public class AuthAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false, length = 36)
    private String uuid;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "is_email_verified", nullable = false)
    private boolean isEmailVerified;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ———————————————— Associations ————————————————

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailVerificationEntity> verificationTokens = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TokenEntity> sessions = new ArrayList<>();

    // ———————————————— Lifecycle callbacks ————————————————

    @PrePersist
    void prePersist() {
        this.uuid = UUID.randomUUID().toString();
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ———————————————— Business methods ————————————————

    public void verifyEmail() {
        this.isEmailVerified = true;
    }

    public List<String> getRoleNames() {
        return roles.stream().map(RoleEntity::getName).toList();
    }

    public void recordLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void addVerificationToken(EmailVerificationEntity token) {
        token.setAccount(this);
        verificationTokens.add(token);
    }

    public void addSession(TokenEntity token) {
        token.setAccount(this);
        sessions.add(token);
    }
}
