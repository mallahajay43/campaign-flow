package me.mallahajay43.campaignflow.audience.dto.response;

import me.mallahajay43.campaignflow.common.enums.ImportStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record ContactImportEvent(
        UUID id,
        UUID tenantId,
        String objectKey,
        ImportStatus status,
        long processedRows,
        String leaseOwner,
        Instant leaseUntil
) {
}
