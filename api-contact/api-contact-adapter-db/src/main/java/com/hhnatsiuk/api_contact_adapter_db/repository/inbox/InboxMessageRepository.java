package com.hhnatsiuk.api_contact_adapter_db.repository.inbox;

import com.hhnatsiuk.api_contact_core.domain.entity.inbox.InboxMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InboxMessageRepository extends JpaRepository<InboxMessageEntity, Long> {
    Optional<InboxMessageEntity> findByMessageId(String messageId);
    boolean existsByMessageId(String messageId);
}
