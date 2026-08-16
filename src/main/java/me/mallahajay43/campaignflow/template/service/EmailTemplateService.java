package me.mallahajay43.campaignflow.template.service;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.common.enums.TemplateStatus;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import me.mallahajay43.campaignflow.template.dto.request.CreateEmailTemplateRequest;
import me.mallahajay43.campaignflow.template.dto.request.UpdateEmailTemplateRequest;
import me.mallahajay43.campaignflow.template.dto.response.EmailTemplateResponse;
import me.mallahajay43.campaignflow.template.entity.EmailTemplate;
import me.mallahajay43.campaignflow.template.mapper.EmailTemplateMapper;
import me.mallahajay43.campaignflow.template.repository.EmailTemplateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final EmailTemplateRepository repository;
    private final TenantContext tenantContext;
    private final EmailTemplateMapper mapper;

    @Transactional
    public EmailTemplateResponse create(CreateEmailTemplateRequest request) {

        EmailTemplate template = EmailTemplate.builder()
                        .tenantId(tenantContext.getTenantId())
                        .name(request.name().trim())
                        .subject(request.subject().trim())
                        .htmlContent(request.htmlContent())
                        .status(TemplateStatus.ACTIVE)
                        .build();

        return mapper.toResponse(repository.save(template));
    }

    @Transactional(readOnly = true)
    public EmailTemplateResponse findById(UUID templateId) {
        return mapper.toResponse(getTemplate(templateId));
    }

    @Transactional(readOnly = true)
    public Page<EmailTemplateResponse> findAll(Pageable pageable) {
        return repository.findAllByTenantId(tenantContext.getTenantId(), pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public EmailTemplateResponse update(UUID templateId, UpdateEmailTemplateRequest request) {

        EmailTemplate template = getTemplate(templateId);

        if (request.name() != null) {
            template.setName(request.name().trim());
        }

        if (request.subject() != null) {
            template.setSubject(request.subject().trim());
        }

        if (request.htmlContent() != null) {
            template.setHtmlContent(request.htmlContent());
        }

        if (request.status() != null) {
            template.setStatus(request.status());
        }

        return mapper.toResponse(repository.save(template));
    }

    @Transactional
    public void delete(UUID templateId) {
        EmailTemplate template = getTemplate(templateId);
        template.setStatus(TemplateStatus.ARCHIVED);
    }

    private EmailTemplate getTemplate(UUID templateId) {
        return repository.findByIdAndTenantId(templateId, tenantContext.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("EMAIL_TEMPLATE", templateId));
    }
}