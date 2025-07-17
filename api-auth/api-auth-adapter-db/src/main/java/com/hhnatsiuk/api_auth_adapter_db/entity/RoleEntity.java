package com.hhnatsiuk.api_auth_adapter_db.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true, length = 50)
        private String name;       // "ROLE_USER", "ROLE_ADMIN"…

        @Column(length = 255)
        private String description;


        // ———————————————— Business method ————————————————
        public static RoleEntity of(String name) {
                return RoleEntity.builder().name(name).build();
        }
}
