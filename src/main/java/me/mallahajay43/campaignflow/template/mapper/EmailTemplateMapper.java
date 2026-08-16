package me.mallahajay43.campaignflow.template.mapper;

import me.mallahajay43.campaignflow.template.dto.request.CreateEmailTemplateRequest;
import me.mallahajay43.campaignflow.template.dto.request.UpdateEmailTemplateRequest;
import me.mallahajay43.campaignflow.template.dto.response.EmailTemplateResponse;
import me.mallahajay43.campaignflow.template.entity.EmailTemplate;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmailTemplateMapper {
    EmailTemplateResponse toResponse(EmailTemplate emailTemplate);

    EmailTemplate toEntityFromCreateContactRequest(CreateEmailTemplateRequest request);

    List<EmailTemplateResponse> toResponseList(List<EmailTemplate> templates);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRecord(UpdateEmailTemplateRequest request, @MappingTarget EmailTemplate template);
}