package com.hhnatsiuk.api_contact_core.domain.entity;

import com.hhnatsiuk.api_contact_core.domain.entity.embeddable.TemplateTargetId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "template_targets")
public class TemplateTargetEntity {

    @EmbeddedId
    private TemplateTargetId id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
