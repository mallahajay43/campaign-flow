package me.mallahajay43.campaignflow.audience.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record CreateContactRequest(

        @NotBlank
        @Email
        @Size(max = 320)
        String email,

        @Size(max = 200)
        String fullName,

        Map<String, Object> attributes
) {
}
