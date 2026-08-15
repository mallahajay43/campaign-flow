package me.mallahajay43.campaignflow.identity.dto.response;

import me.mallahajay43.campaignflow.common.enums.TenantStatus;

import java.util.UUID;

public record TenantResponse(
    UUID id,
    String name,
    String email,
    String timezone,
    TenantStatus status
) {
}
