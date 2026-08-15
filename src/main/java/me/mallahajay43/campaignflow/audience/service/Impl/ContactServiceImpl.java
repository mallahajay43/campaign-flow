package me.mallahajay43.campaignflow.audience.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.audience.dto.request.CreateContactRequest;
import me.mallahajay43.campaignflow.audience.dto.request.UpdateContactRequest;
import me.mallahajay43.campaignflow.audience.dto.response.ContactResponse;
import me.mallahajay43.campaignflow.audience.entity.Contact;
import me.mallahajay43.campaignflow.audience.mapper.ContactMapper;
import me.mallahajay43.campaignflow.audience.repository.ContactRepository;
import me.mallahajay43.campaignflow.audience.service.ContactService;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.common.enums.ContactStatus;
import me.mallahajay43.campaignflow.common.exceptions.DuplicateResourceException;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final TenantContext tenantContext;

    @Override
    @Transactional
    public ContactResponse create(CreateContactRequest request) {

        UUID tenantId = tenantContext.getTenantId();
        String email = normalizeEmail(request.email());

        Optional<Contact> optionalContact = contactRepository.findByEmailIgnoreCaseAndTenantId(email, tenantId);
        if (optionalContact.isPresent()) {
            throw new DuplicateResourceException("CONTACT",
                    "Email already exists with tenant id " + tenantId + " email: "  + request.email());
        }

        Contact contact = contactMapper.toEntityFromCreateContactRequest(request);
        contact.setTenantId(tenantId);
        contact.setStatus(ContactStatus.ACTIVE);
        contact.setEmail(email);
        contact = contactRepository.save(contact);

        return contactMapper.toResponse(contact);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactResponse> findAll(Pageable pageable) {
        UUID tenantId = tenantContext.getTenantId();
        return contactRepository.findAllByTenantId(tenantId, pageable)
                .map(contactMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse findById(UUID contactId) {
        UUID tenantId = tenantContext.getTenantId();
        return contactMapper.toResponse(getContact(contactId, tenantId));
    }

    @Override
    @Transactional
    public ContactResponse update(UUID contactId, UpdateContactRequest request) {

        UUID tenantId = tenantContext.getTenantId();
        Contact contact = getContact(contactId, tenantId);

        contactMapper.updateEntityFromRecord(request, contact);
        if (request.email() != null) {
            String email = normalizeEmail(request.email());
            contactRepository.findByEmailIgnoreCaseAndTenantId(email, tenantId)
                .filter(existing -> !existing.getId().equals(contactId))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("CONTACT",
                        "Email already exists with tenant id " + tenantId + " email: "  + request.email());
                });

            contact.setEmail(email);
        }

        contact = contactRepository.save(contact);

        return contactMapper.toResponse(contact);
    }

    @Override
    @Transactional
    public void delete(UUID contactId) {

        UUID tenantId = tenantContext.getTenantId();
        Contact contact = getContact(contactId, tenantId);
        contactRepository.delete(contact);
    }

    private String normalizeEmail(String email) {
        return email.trim()
                .toLowerCase(Locale.ROOT);
    }

    private Contact getContact(UUID contactId, UUID tenantId) {
        return contactRepository.findByIdAndTenantId(contactId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("CONTACT", contactId.toString()));
    }
}
