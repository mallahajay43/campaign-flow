package me.mallahajay43.campaignflow.audience.repository;

import me.mallahajay43.campaignflow.audience.entity.Contact;
import me.mallahajay43.campaignflow.common.enums.ContactStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {
    Optional<Contact> findByIdAndTenantId(UUID id, UUID tenantId);

    Optional<Contact> findByEmailIgnoreCaseAndTenantId(String email, UUID tenantId);

    Page<Contact> findAllByTenantId(UUID tenantId, Pageable pageable);

    boolean existsByEmailIgnoreCaseAndTenantId(String email, UUID tenantId);

    Page<Contact> findAllByTenantIdAndStatus(UUID tenantId, ContactStatus status, Pageable pageable);
}
