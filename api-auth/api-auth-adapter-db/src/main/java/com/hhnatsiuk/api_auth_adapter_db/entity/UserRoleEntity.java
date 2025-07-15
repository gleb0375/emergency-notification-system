package com.hhnatsiuk.api_auth_adapter_db.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "user_roles")
@IdClass(UserRoleEntity.Key.class)
@Getter @Setter @NoArgsConstructor
public class UserRoleEntity {
    @Id
    @Column(name = "credentials_id")
    private Long credentialsId;

    @Id
    @Column(name = "role_id")
    private Long roleId;

    @NoArgsConstructor(staticName = "of")
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Key implements Serializable {
        private Long credentialsId;
        private Long roleId;
    }
}
