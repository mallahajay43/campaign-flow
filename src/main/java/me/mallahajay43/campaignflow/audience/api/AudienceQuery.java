package me.mallahajay43.campaignflow.audience.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface AudienceQuery {

    Page<ContactProjection> findActiveContacts(UUID tenantId, Pageable pageable);

    boolean isSuppressed(UUID tenantId, String email);
}
