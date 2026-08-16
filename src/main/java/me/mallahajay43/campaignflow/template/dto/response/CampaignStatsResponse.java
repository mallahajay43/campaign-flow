package me.mallahajay43.campaignflow.template.dto.response;

import me.mallahajay43.campaignflow.common.enums.CampaignStatus;

import java.util.UUID;

public record CampaignStatsResponse(
        UUID campaignId,
        CampaignStatus status,
        long total,
        long sent,
        long failed,
        long suppressed
) {
}
