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
@Table(name = "contact_groups")
public class ContactGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "contact_group_uuid", nullable = false, length = 36, unique = true)
    private String contactGroupUuid;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_by_account_uuid", nullable = false, length = 36)
    private String createdByAccountUuid;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
