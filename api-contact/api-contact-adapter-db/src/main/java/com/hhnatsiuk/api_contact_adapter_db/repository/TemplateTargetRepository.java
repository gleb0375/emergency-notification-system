package com.hhnatsiuk.api_contact_adapter_db.repository;


import com.hhnatsiuk.api_contact_core.domain.entity.TemplateTargetEntity;
import com.hhnatsiuk.api_contact_core.domain.entity.embeddable.TemplateTargetId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateTargetRepository extends JpaRepository<TemplateTargetEntity, TemplateTargetId> {

    List<TemplateTargetEntity> findAllByIdTemplateId(Integer templateId);

    List<TemplateTargetEntity> findAllByIdGroupId(Integer groupId);

    boolean existsByIdTemplateIdAndIdGroupId(Integer templateId, Integer groupId);

    long deleteByIdTemplateIdAndIdGroupId(Integer templateId, Integer groupId);
}
