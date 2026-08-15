package me.mallahajay43.campaignflow.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.mallahajay43.campaignflow.common.annotation.ValidTimezone;
import me.mallahajay43.campaignflow.common.enums.UserRole;

public record UpdateUserRequest(
        @Size(max = 50, message = "Name should not be more than 100 characters long")
        String fullName,

        @Email
        String email,

        @Size(min = 8, message = "Password length should be at least 8 characters")
        String password
) {}
