package me.mallahajay43.campaignflow.audience.dto.response;

import me.mallahajay43.campaignflow.common.enums.SegmentType;

import java.util.Map;
import java.util.UUID;

public record SegmentResponse(
        UUID id,
        String name,
        String description,
        SegmentType type,
        Map<String, Object> definition
) {
}
