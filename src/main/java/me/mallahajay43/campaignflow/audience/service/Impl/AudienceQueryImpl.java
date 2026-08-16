package me.mallahajay43.campaignflow.audience.service.Impl;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.audience.api.AudienceQuery;
import me.mallahajay43.campaignflow.audience.api.ContactProjection;
import me.mallahajay43.campaignflow.audience.entity.Contact;
import me.mallahajay43.campaignflow.audience.mapper.ContactMapper;
import me.mallahajay43.campaignflow.audience.repository.ContactRepository;
import me.mallahajay43.campaignflow.audience.repository.SuppressionEntryRepository;
import me.mallahajay43.campaignflow.common.enums.ContactStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class AudienceQueryImpl implements AudienceQuery {

    private final ContactRepository contactRepository;
    private final SuppressionEntryRepository suppressionRepository;
    private final ContactMapper contactMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ContactProjection> findActiveContacts(UUID tenantId, Pageable pageable) {

        return contactRepository.findAllByTenantIdAndStatus(
                        tenantId, ContactStatus.ACTIVE, pageable)
                .map(contactMapper::toProjectionEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSuppressed(UUID tenantId, String email) {

        if (email == null || email.isBlank()) {
            return false;
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        return suppressionRepository.existsByTenantIdAndEmailIgnoreCase(
                        tenantId, normalizedEmail);
    }

    private ContactProjection toProjection(Contact contact) {

        return new ContactProjection(
                contact.getId(),
                contact.getEmail(),
                contact.getFullName(),
                contact.getAttributes()
        );
    }
}