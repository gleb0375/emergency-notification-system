package com.hhnatsiuk.api_auth_adapter_db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "credentials")
@Getter
@NoArgsConstructor
public class CredentialEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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


    // ———————————————— Associations ————————————————

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "credentials_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new LinkedHashSet<>();

    @OneToOne(mappedBy = "credential", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserEntity userProfile;

    @OneToMany(mappedBy = "credential", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailVerificationTokenEntity> verificationTokens = new ArrayList<>();

    @OneToMany(mappedBy = "credential", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshTokenEntity> refreshTokens = new ArrayList<>();

    // ———————————————— lifecycle callbacks ————————————————

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }
    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ———————————————— Business methods ————————————————

    public void verifyEmail() {
        this.emailVerified = true;
    }
    public void recordLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void setUserProfile(UserEntity profile) {
        profile.setCredential(this);
        this.userProfile = profile;
    }

    public void addVerificationToken(EmailVerificationTokenEntity token) {
        token.setCredential(this);
        verificationTokens.add(token);
    }

    public void addRefreshToken(RefreshTokenEntity token) {
        token.setCredential(this);
        refreshTokens.add(token);
    }
}
