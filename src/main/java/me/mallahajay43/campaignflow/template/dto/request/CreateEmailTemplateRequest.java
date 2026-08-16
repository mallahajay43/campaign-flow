package me.mallahajay43.campaignflow.template.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import me.mallahajay43.campaignflow.common.entity.BaseEntity;
import me.mallahajay43.campaignflow.common.enums.TemplateStatus;

import java.util.UUID;

public record CreateEmailTemplateRequest(

        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Size(max = 255)
        String subject,

        @NotBlank
        String htmlContent
) {
}