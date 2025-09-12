package com.hhnatsiuk.api_contact_core.domain.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TemplateTargetId implements Serializable {

    @Column(name = "template_id", nullable = false)
    private Integer templateId;

    @Column(name = "group_id", nullable = false)
    private Integer groupId;
}
