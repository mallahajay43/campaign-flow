package me.mallahajay43.campaignflow.audience.api;

import java.util.Map;
import java.util.UUID;

public record ContactProjection(
        UUID id,
        String email,
        String fullName,
        Map<String, Object> attributes
) {
}
