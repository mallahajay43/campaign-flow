package me.mallahajay43.campaignflow.audience.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import me.mallahajay43.campaignflow.common.enums.ContactStatus;

import java.util.Map;

public record CreateTagRequest(

        @NotBlank
        @Size(max = 100)
        String name,

        @Size(max = 255)
        String description

) {
}
