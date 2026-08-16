package me.mallahajay43.campaignflow.template.dto.response;

import me.mallahajay43.campaignflow.common.enums.CampaignAudienceType;
import me.mallahajay43.campaignflow.common.enums.CampaignStatus;
import me.mallahajay43.campaignflow.common.enums.TemplateStatus;

import java.time.Instant;
import java.util.UUID;

public record CampaignResponse(
        UUID id,
        String name,
        String subject,
        UUID templateId,
        CampaignAudienceType audienceType,
        CampaignStatus status,
        Instant startedAt,
        Instant completedAt,
        Instant createdAt
) {
}