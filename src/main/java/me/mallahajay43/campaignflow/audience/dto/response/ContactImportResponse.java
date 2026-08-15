package me.mallahajay43.campaignflow.audience.dto.response;

import me.mallahajay43.campaignflow.common.enums.ImportStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContactImportResponse(
        UUID id,
        String fileName,
        ImportStatus status,
        long fileSize,
        long totalRecords,
        long successCount,
        long failedCount,
        long processedRows,
        long skippedCount,
        String errorMessage,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        LocalDateTime createdAt
) {
}
