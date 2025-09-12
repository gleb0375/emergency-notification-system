package com.hhnatsiuk.api_contact_core.domain.entity.embeddable;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GroupMembershipId implements Serializable {

    @Column(name = "group_id", nullable = false)
    private Integer groupId;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;
}