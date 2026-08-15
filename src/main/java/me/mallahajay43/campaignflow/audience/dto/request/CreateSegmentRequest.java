package me.mallahajay43.campaignflow.audience.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.mallahajay43.campaignflow.common.enums.ContactStatus;

import java.util.Map;

public record CreateSegmentRequest(

        @NotBlank
        String name,

        String description,

        @NotNull
        Map<String, Object> definition

) {
}
