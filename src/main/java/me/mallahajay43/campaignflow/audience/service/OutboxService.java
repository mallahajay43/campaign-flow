package me.mallahajay43.campaignflow.audience.service;

import java.util.UUID;

public interface OutboxService {
    public void enqueue(
            UUID eventId,
            UUID tenantId,
            UUID aggregateId,
            String aggregateType,
            String eventType,
            String topic,
            String eventKey,
            Object payload
    );
}
