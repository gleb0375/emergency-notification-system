package com.hhnatsiuk.api_contact_core.domain.entity;

import com.hhnatsiuk.api_contact_core.domain.entity.embeddable.GroupMembershipId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_memberships")
public class GroupMembershipEntity {

    @EmbeddedId
    private GroupMembershipId id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
