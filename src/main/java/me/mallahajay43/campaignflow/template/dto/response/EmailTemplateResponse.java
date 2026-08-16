package me.mallahajay43.campaignflow.template.dto.response;

import me.mallahajay43.campaignflow.common.enums.TemplateStatus;

import java.time.Instant;
import java.util.UUID;

public record EmailTemplateResponse(
        UUID id,
        String name,
        String subject,
        String htmlContent,
        TemplateStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}