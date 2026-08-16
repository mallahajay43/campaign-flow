package me.mallahajay43.campaignflow.template.service;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.common.enums.CampaignStatus;
import me.mallahajay43.campaignflow.common.enums.EventAggregateType;
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
public class CampaignSendTransactionalService {

    private final CampaignRepository campaignRepository;
    private final OutboxEventPublisher outboxEventPublisher;
    private final CampaignMapper campaignMapper;

    @Transactional
    public CampaignResponse send(UUID tenantId, UUID campaignId) {

        Campaign campaign = campaignRepository
                        .findByIdAndTenantId(campaignId, tenantId)
                        .orElseThrow(() -> new ResourceNotFoundException("CAMPAIGN_SEND", campaignId));

        if (campaign.getStatus() != CampaignStatus.DRAFT) {
            return campaignMapper.toResponse(campaign);
        }

        campaign.setStatus(CampaignStatus.QUEUED);

        outboxEventPublisher.publish(EventAggregateType.CAMPAIGN_STARTED, campaignId, "CAMPAIGN_STARTED",
                Map.of("campaignId", campaignId.toString(),
                        "tenantId", tenantId.toString(),
                        "campaignStatus", CampaignStatus.QUEUED.name(),
                        "occurredAt", Instant.now().toString()
                )
        );

        return campaignMapper.toResponse(campaignRepository.save(campaign));
    }
}