package com.hhnatsiuk.api_auth_adapter_db.repository.outbox;

import com.hhnatsiuk.api_auth_core.outbox.OutboxEventEntity;
import com.hhnatsiuk.api_auth_core.outbox.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEventEntity, Long> {

    Optional<OutboxEventEntity> findByEventId(String eventId);

    List<OutboxEventEntity> findTop100ByStatusInOrderByOccurredAtAsc(Collection<OutboxStatus> statuses);
}
