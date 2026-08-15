package me.mallahajay43.campaignflow.audience.dto.response;

import me.mallahajay43.campaignflow.common.enums.ContactStatus;

import java.util.Map;
import java.util.UUID;

public record ContactResponse(
        UUID id,
        String email,
        String fullName,
        ContactStatus status,
        Map<String, Object> attributes
) {
}
