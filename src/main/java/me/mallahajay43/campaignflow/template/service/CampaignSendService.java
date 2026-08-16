package me.mallahajay43.campaignflow.template.service;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.common.enums.CampaignStatus;
import me.mallahajay43.campaignflow.common.enums.EventAggregateType;
import me.mallahajay43.campaignflow.common.exceptions.BusinessRuleViolationException;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import me.mallahajay43.campaignflow.common.outbox.OutboxEventPublisher;
import me.mallahajay43.campaignflow.template.dto.response.CampaignResponse;
import me.mallahajay43.campaignflow.template.entity.Campaign;
import me.mallahajay43.campaignflow.template.mapper.CampaignMapper;
import me.mallahajay43.campaignflow.template.redis.IdempotencyService;
import me.mallahajay43.campaignflow.template.repository.CampaignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignSendService {

    private final CampaignRepository campaignRepository;
    private final OutboxEventPublisher outboxEventPublisher;
    private final TenantContext tenantContext;
    private final IdempotencyService idempotencyService;
    private final CampaignService campaignService;
    private final CampaignSendTransactionalService transactionalService;
    private final CampaignMapper campaignMapper;

    public CampaignResponse send(UUID campaignId, String idempotencyKey) {

        UUID tenantId = tenantContext.getTenantId();

        boolean acquired = idempotencyService.acquire(tenantId,campaignId, idempotencyKey);

        if (!acquired) {
            return campaignService.findById(campaignId);
        }

        try {
            return transactionalService.send(tenantId, campaignId);
        } catch (Exception exception) {
            idempotencyService.release(tenantId, campaignId, idempotencyKey);
            throw exception;
        }
    }

}