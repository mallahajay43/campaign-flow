package me.mallahajay43.campaignflow.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.mallahajay43.campaignflow.common.annotation.ValidTimezone;
import me.mallahajay43.campaignflow.common.enums.UserRole;

public record UpdateTenantRequest(
        @Size(min = 2, max = 150)
        String name,

        @Email
        @Size(max = 255)
        String email,

        @ValidTimezone
        String timezone
) {}
