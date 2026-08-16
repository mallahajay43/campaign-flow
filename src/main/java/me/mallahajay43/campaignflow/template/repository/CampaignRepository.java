package me.mallahajay43.campaignflow.template.repository;

import me.mallahajay43.campaignflow.template.entity.Campaign;
import me.mallahajay43.campaignflow.template.entity.EmailTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    Optional<Campaign> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<Campaign> findAllByTenantId(UUID tenantId, Pageable pageable);
}