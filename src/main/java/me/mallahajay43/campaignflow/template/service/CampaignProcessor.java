package me.mallahajay43.campaignflow.template.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.audience.api.AudienceQuery;
import me.mallahajay43.campaignflow.audience.api.ContactProjection;
import me.mallahajay43.campaignflow.common.enums.CampaignStatus;
import me.mallahajay43.campaignflow.common.enums.RecipientStatus;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import me.mallahajay43.campaignflow.identity.api.IdentityService;
import me.mallahajay43.campaignflow.template.entity.Campaign;
import me.mallahajay43.campaignflow.template.entity.CampaignRecipient;
import me.mallahajay43.campaignflow.template.entity.EmailTemplate;
import me.mallahajay43.campaignflow.template.rederer.EmailRenderer;
import me.mallahajay43.campaignflow.template.repository.CampaignRecipientRepository;
import me.mallahajay43.campaignflow.template.repository.CampaignRepository;
import me.mallahajay43.campaignflow.template.repository.EmailTemplateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignProcessor {

    private final CampaignRepository campaignRepository;
    private final CampaignRecipientRepository recipientRepository;
    private final EmailTemplateRepository templateRepository;
    private final AudienceQuery audienceQuery;
    private final EmailProvider emailProvider;
    private final EmailRenderer renderer;
    private final IdentityService identityService;

    @Transactional
    public void markProcessing(Campaign campaign) {
        campaign.setStatus(CampaignStatus.PROCESSING);
        campaign.setStartedAt(Instant.now());
    }

    public void process(UUID tenantId, UUID campaignId) {

        Campaign campaign = campaignRepository
                        .findByIdAndTenantId(campaignId, tenantId)
                        .orElseThrow(() -> new ResourceNotFoundException("CAMPAIGN", campaignId));

        // Idempotent Kafka processing.
        if (isCompleted(campaign)) {return;}

        campaign.setStatus(CampaignStatus.PROCESSING);

        campaign.setStartedAt(
                Optional.ofNullable(campaign.getStartedAt())
                        .orElse(Instant.now()));

        campaignRepository.save(campaign);

        EmailTemplate template = templateRepository
                        .findByIdAndTenantId(campaign.getTemplateId(), tenantId)
                        .orElseThrow(() -> new ResourceNotFoundException("EMAIL_TEMPLATE", campaignId));

        int pageNumber = 0;

        Page<ContactProjection> contacts;

        do {
            contacts = audienceQuery.findActiveContacts(
                            tenantId,
                            PageRequest.of(pageNumber, 100)
                    );

            for (ContactProjection contact : contacts.getContent()) {
                processContact(campaign, template, contact);
            }

            pageNumber++;

        } while (contacts.hasNext());

        completeCampaign(campaign);
    }

    private void processContact(Campaign campaign, EmailTemplate template, ContactProjection contact) {

        Optional<CampaignRecipient> existing =
                recipientRepository.findByCampaignIdAndContactId(campaign.getId(), contact.id());

        /*
         * Kafka retry:
         * don't send emails that already succeeded.
         */
        if (existing.isPresent() && existing.get().getStatus() == RecipientStatus.SENT) {
            return;
        }

        CampaignRecipient recipient =
                existing.orElseGet(() ->
                        CampaignRecipient.builder()
                                .tenantId(campaign.getTenantId())
                                .campaignId(campaign.getId())
                                .contactId(contact.id())
                                .email(contact.email())
                                .fullName(contact.fullName())
                                .status(RecipientStatus.PENDING)
                                .build()
                );

        /*
         * Suppression.
         */
        if (audienceQuery.isSuppressed(campaign.getTenantId(), contact.email())) {
            recipient.setStatus(RecipientStatus.SUPPRESSED);
            recipientRepository.save(recipient);
            return;
        }

        try {

            String fromEmail = identityService.getTenantEmail(campaign.getTenantId());

            String html = renderer.render(template.getHtmlContent(), contact);
            emailProvider.send(contact.email(), campaign.getSubject(), html, fromEmail);

            recipient.setStatus(RecipientStatus.SENT);
            recipient.setSentAt(Instant.now());
            recipient.setLastError(null);

        } catch (Exception exception) {
            recipient.setStatus(RecipientStatus.FAILED);
            recipient.setFailedAt(Instant.now());
            recipient.setLastError(exception.getMessage());

            log.error("Failed sending campaign {} to {}", campaign.getId(), contact.email(), exception);
        }

        recipientRepository.save(recipient);
    }

    private void completeCampaign(Campaign campaign) {

        long sent = recipientRepository.countByCampaignIdAndStatus(campaign.getId(), RecipientStatus.SENT);
        long failed = recipientRepository.countByCampaignIdAndStatus(campaign.getId(), RecipientStatus.FAILED);

        if (sent == 0 && failed > 0) {
            campaign.setStatus(CampaignStatus.FAILED);
        } else if (failed > 0) {
            campaign.setStatus(CampaignStatus.PARTIALLY_FAILED);
        } else {
            campaign.setStatus(CampaignStatus.COMPLETED);
        }

        campaign.setCompletedAt(
                Instant.now()
        );

        campaignRepository.save(campaign);
    }

    private boolean isCompleted(Campaign campaign) {

        return campaign.getStatus() == CampaignStatus.COMPLETED ||
                campaign.getStatus() == CampaignStatus.PARTIALLY_FAILED;
    }
}
