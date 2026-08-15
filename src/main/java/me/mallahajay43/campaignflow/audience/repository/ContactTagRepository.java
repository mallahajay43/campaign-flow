package me.mallahajay43.campaignflow.audience.repository;

import me.mallahajay43.campaignflow.audience.entity.ContactTag;
import me.mallahajay43.campaignflow.audience.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContactTagRepository extends JpaRepository<ContactTag, UUID> {
    boolean existsByTenantIdAndContactIdAndTagId(UUID tenantId, UUID contactId, UUID tagId);

    List<ContactTag> findAllByTenantIdAndContactId(UUID tenantId, UUID contactId);

    void deleteByTenantIdAndContactIdAndTagId(UUID tenantId, UUID contactId, UUID tagId);
}
