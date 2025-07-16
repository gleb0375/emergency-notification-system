package com.hhnatsiuk.api_auth_adapter_db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "credentials")
@Getter
@Setter
@NoArgsConstructor
public class CredentialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean isEmailVerified;

    @Column(nullable = false)
    private Date createdAt;

    private Date updatedAt;

}
