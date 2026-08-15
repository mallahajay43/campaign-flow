package me.mallahajay43.campaignflow.audience.repository;

import me.mallahajay43.campaignflow.audience.entity.SuppressionEntry;
import me.mallahajay43.campaignflow.audience.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SuppressionEntryRepository extends JpaRepository<SuppressionEntry, UUID> {
    boolean existsByTenantIdAndEmailIgnoreCase(UUID tenantId, String email);

    Optional<SuppressionEntry> findByTenantIdAndEmailIgnoreCase(UUID tenantId, String email);

    List<SuppressionEntry> findAllByTenantId(UUID tenantId);

    Optional<SuppressionEntry> findByIdAndTenantId(UUID id, UUID tenantId);
}
