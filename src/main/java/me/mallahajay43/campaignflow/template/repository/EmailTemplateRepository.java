package me.mallahajay43.campaignflow.template.repository;

import me.mallahajay43.campaignflow.template.entity.EmailTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, UUID> {

    Optional<EmailTemplate> findByIdAndTenantId(UUID id, UUID tenantId);

    Page<EmailTemplate> findAllByTenantId(UUID tenantId, Pageable pageable);
}