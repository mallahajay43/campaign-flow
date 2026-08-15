package me.mallahajay43.campaignflow.audience.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContactImportRequestedEvent(
        UUID eventId,
        UUID importId,
        UUID tenantId,
        LocalDateTime occurredAt
) {
}