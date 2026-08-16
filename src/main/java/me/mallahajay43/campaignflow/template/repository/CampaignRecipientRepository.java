package me.mallahajay43.campaignflow.template.repository;

import me.mallahajay43.campaignflow.common.enums.RecipientStatus;
import me.mallahajay43.campaignflow.template.entity.Campaign;
import me.mallahajay43.campaignflow.template.entity.CampaignRecipient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface CampaignRecipientRepository extends JpaRepository<CampaignRecipient, UUID> {

    boolean existsByCampaignIdAndContactId(UUID campaignId, UUID contactId);

    long countByCampaignId(UUID campaignId);

    Optional<CampaignRecipient> findByCampaignIdAndContactId(UUID campaignId, UUID contactId);

    long countByCampaignIdAndStatus(UUID campaignId, RecipientStatus status);
}