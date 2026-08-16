package me.mallahajay43.campaignflow.identity.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import me.mallahajay43.campaignflow.common.enums.UserRole;
import me.mallahajay43.campaignflow.common.enums.UserStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String fullName,
        UserRole role,
        UserStatus status,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Instant createdAt
) {
}
