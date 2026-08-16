package me.mallahajay43.campaignflow.template.service;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.common.enums.CampaignAudienceType;
import me.mallahajay43.campaignflow.common.enums.CampaignStatus;
import me.mallahajay43.campaignflow.common.enums.TemplateStatus;
import me.mallahajay43.campaignflow.common.exceptions.BusinessRuleViolationException;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import me.mallahajay43.campaignflow.template.dto.request.CreateCampaignRequest;
import me.mallahajay43.campaignflow.template.dto.request.CreateEmailTemplateRequest;
import me.mallahajay43.campaignflow.template.dto.request.UpdateEmailTemplateRequest;
import me.mallahajay43.campaignflow.template.dto.response.CampaignResponse;
import me.mallahajay43.campaignflow.template.dto.response.EmailTemplateResponse;
import me.mallahajay43.campaignflow.template.entity.Campaign;
import me.mallahajay43.campaignflow.template.entity.EmailTemplate;
import me.mallahajay43.campaignflow.template.mapper.CampaignMapper;
import me.mallahajay43.campaignflow.template.mapper.EmailTemplateMapper;
import me.mallahajay43.campaignflow.template.repository.CampaignRepository;
import me.mallahajay43.campaignflow.template.repository.EmailTemplateRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final EmailTemplateRepository templateRepository;
    private final TenantContext tenantContext;
    private final CampaignMapper mapper;

    @Transactional
    public CampaignResponse create(CreateCampaignRequest request) {

        UUID tenantId = tenantContext.getTenantId();

        EmailTemplate template = templateRepository
                        .findByIdAndTenantId(request.templateId(), tenantId)
                        .orElseThrow(() -> new ResourceNotFoundException("CAMPAIGN_TEMPLATE", request.templateId()));

        if (template.getStatus() != TemplateStatus.ACTIVE) {
            throw new BusinessRuleViolationException("INACTIVE_TEMPLATE", "Requested template is not active");
        }

        String subject = request.subject() != null && !request.subject().isBlank()
                        ? request.subject() : template.getSubject();

        Campaign campaign = Campaign.builder()
                        .tenantId(tenantId)
                        .name(request.name())
                        .subject(subject)
                        .templateId(template.getId())
                        .audienceType(CampaignAudienceType.ALL_CONTACTS)
                        .status(CampaignStatus.DRAFT)
                        .build();

        return mapper.toResponse(campaignRepository.save(campaign));
    }

    @Transactional(readOnly = true)
    public CampaignResponse findById(UUID campaignId) {
        return mapper.toResponse(getCampaign(campaignId));
    }

    @Transactional(readOnly = true)
    public Page<CampaignResponse> findAll(Pageable pageable) {
        return campaignRepository.findAllByTenantId(tenantContext.getTenantId(), pageable)
                .map(mapper::toResponse);
    }

    private Campaign getCampaign(UUID campaignId) {
        return campaignRepository.findByIdAndTenantId(campaignId, tenantContext.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("CAMPAIGN_TEMPLATE", campaignId));
    }
}