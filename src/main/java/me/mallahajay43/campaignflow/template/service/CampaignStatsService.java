package me.mallahajay43.campaignflow.template.service;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.common.enums.RecipientStatus;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import me.mallahajay43.campaignflow.template.dto.response.CampaignStatsResponse;
import me.mallahajay43.campaignflow.template.entity.Campaign;
import me.mallahajay43.campaignflow.template.repository.CampaignRecipientRepository;
import me.mallahajay43.campaignflow.template.repository.CampaignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignStatsService {

    private final CampaignRepository campaignRepository;
    private final CampaignRecipientRepository recipientRepository;
    private final TenantContext tenantContext;

    @Transactional(readOnly = true)
    public CampaignStatsResponse get(UUID campaignId) {

        UUID tenantId = tenantContext.getTenantId();

        Campaign campaign = campaignRepository.findByIdAndTenantId(campaignId, tenantId)
                        .orElseThrow(() -> new ResourceNotFoundException("CAMPAIGN_STATS", campaignId.toString()));

        return new CampaignStatsResponse(
                campaignId,
                campaign.getStatus(),
                recipientRepository.countByCampaignId(campaignId),
                recipientRepository.countByCampaignIdAndStatus(campaignId, RecipientStatus.SENT),
                recipientRepository.countByCampaignIdAndStatus(campaignId, RecipientStatus.FAILED),
                recipientRepository.countByCampaignIdAndStatus(campaignId, RecipientStatus.SUPPRESSED)
        );
    }
}
