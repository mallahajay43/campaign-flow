package me.mallahajay43.campaignflow.common.outbox;

import me.mallahajay43.campaignflow.common.entity.OutboxEvent;
import me.mallahajay43.campaignflow.common.enums.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
