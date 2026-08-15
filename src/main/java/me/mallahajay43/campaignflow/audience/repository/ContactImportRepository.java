package me.mallahajay43.campaignflow.audience.repository;

import me.mallahajay43.campaignflow.audience.entity.Contact;
import me.mallahajay43.campaignflow.audience.entity.ContactImport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContactImportRepository extends JpaRepository<ContactImport, UUID> {
    Optional<ContactImport> findByIdAndTenantId(UUID importId, UUID tenantId);
}
