package me.mallahajay43.campaignflow.audience.dto.response;

import me.mallahajay43.campaignflow.common.enums.SegmentType;
import me.mallahajay43.campaignflow.common.enums.SuppressionReason;
import me.mallahajay43.campaignflow.common.enums.SuppressionSource;

import java.util.Map;
import java.util.UUID;

public record SuppressionResponse(
        UUID id,
        String email,
        SuppressionReason reason,
        SuppressionSource source
) {
}
