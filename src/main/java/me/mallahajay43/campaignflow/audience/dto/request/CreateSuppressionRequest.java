package me.mallahajay43.campaignflow.audience.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.mallahajay43.campaignflow.common.enums.ContactStatus;
import me.mallahajay43.campaignflow.common.enums.SuppressionReason;

import java.util.Map;

public record CreateSuppressionRequest(

        @NotBlank
        @Email
        String email,

        @NotNull
        SuppressionReason reason

) {
}
