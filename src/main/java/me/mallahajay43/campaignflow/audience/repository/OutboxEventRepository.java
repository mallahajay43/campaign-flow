package me.mallahajay43.campaignflow.audience.repository;

import me.mallahajay43.campaignflow.audience.entity.ContactImportFile;
import me.mallahajay43.campaignflow.audience.entity.OutboxEvent;
import me.mallahajay43.campaignflow.common.enums.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
