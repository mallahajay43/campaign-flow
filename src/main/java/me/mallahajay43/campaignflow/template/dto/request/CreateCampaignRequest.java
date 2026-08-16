package me.mallahajay43.campaignflow.template.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateCampaignRequest(

        @NotBlank
        @Size(max = 200)
        String name,

        @NotNull
        UUID templateId,

        @Size(max = 255)
        String subject
) {
}