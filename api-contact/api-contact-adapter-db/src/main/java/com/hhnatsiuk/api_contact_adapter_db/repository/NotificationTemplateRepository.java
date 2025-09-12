package com.hhnatsiuk.api_contact_adapter_db.repository;

import com.hhnatsiuk.api_contact_core.domain.entity.NotificationTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplateEntity, Integer> {
}
