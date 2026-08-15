package me.mallahajay43.campaignflow.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.mallahajay43.campaignflow.common.annotation.ValidTimezone;

public record TenantSignupRequest(
        @NotNull(message = "Tenant name is required")
        @Size(max = 50, message = "Tenant name should not be more than 100 characters long")
        String tenantName,

        @NotNull(message = "Name is required (user full name)")
        @Size(max = 50, message = "Name should not be more than 100 characters long")
        String fullName,

        @Email
        @NotNull(message = "Email is required")
        String email,

        @NotNull(message = "Password is Required")
        @Size(min = 8, message = "Password length should be at least 8 characters")
        String password,

        @ValidTimezone
        String timezone
) {
}
