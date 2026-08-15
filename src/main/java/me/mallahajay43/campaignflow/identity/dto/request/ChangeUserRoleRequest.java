package me.mallahajay43.campaignflow.identity.dto.request;

import jakarta.validation.constraints.NotNull;
import me.mallahajay43.campaignflow.common.enums.UserRole;

public record ChangeUserRoleRequest(
        @NotNull UserRole role
) {

}
