package me.mallahajay43.campaignflow.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.mallahajay43.campaignflow.common.enums.UserRole;

public record UserCreateRequest(
        @NotNull(message = "Name is required (user full name)")
        @Size(max = 50, message = "Name should not be more than 100 characters long")
        String fullName,

        @Email
        @NotNull(message = "Email is required")
        String email,

        @NotNull(message = "Password is Required")
        @Size(min = 8, message = "Password length should be at least 8 characters")
        String password,

        @NotNull(message = "User role is required")
        UserRole role
) {
}
