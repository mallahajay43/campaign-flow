package me.mallahajay43.campaignflow.template.dto.request;

import jakarta.validation.constraints.Size;
import me.mallahajay43.campaignflow.common.enums.TemplateStatus;

public record UpdateEmailTemplateRequest(

        @Size(max = 150)
        String name,

        @Size(max = 255)
        String subject,

        String htmlContent,

        TemplateStatus status
) {
}