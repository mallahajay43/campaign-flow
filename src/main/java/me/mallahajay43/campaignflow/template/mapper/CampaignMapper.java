package me.mallahajay43.campaignflow.template.mapper;

import me.mallahajay43.campaignflow.template.dto.request.CreateEmailTemplateRequest;
import me.mallahajay43.campaignflow.template.dto.request.UpdateEmailTemplateRequest;
import me.mallahajay43.campaignflow.template.dto.response.CampaignResponse;
import me.mallahajay43.campaignflow.template.dto.response.EmailTemplateResponse;
import me.mallahajay43.campaignflow.template.entity.Campaign;
import me.mallahajay43.campaignflow.template.entity.EmailTemplate;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CampaignMapper {
    CampaignResponse toResponse(Campaign campaign);
    List<CampaignResponse> toResponseList(List<Campaign> campaigns);
}