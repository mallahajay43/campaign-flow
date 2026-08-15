package me.mallahajay43.campaignflow.audience.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import me.mallahajay43.campaignflow.common.enums.ContactStatus;

import java.util.Map;

public record UpdateContactRequest(

        @Email
        @Size(max = 320)
        String email,

        @Size(max = 200)
        String fullName,

        ContactStatus status,

        Map<String, Object> attributes
) {
}
