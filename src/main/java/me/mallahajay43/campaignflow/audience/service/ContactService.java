package me.mallahajay43.campaignflow.audience.service;

import me.mallahajay43.campaignflow.audience.dto.request.CreateContactRequest;
import me.mallahajay43.campaignflow.audience.dto.request.UpdateContactRequest;
import me.mallahajay43.campaignflow.audience.dto.response.ContactResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ContactService {
    ContactResponse create(CreateContactRequest request);

    Page<ContactResponse> findAll(Pageable pageable);

    ContactResponse findById(UUID contactId);

    ContactResponse update(UUID contactId, UpdateContactRequest request);

    void delete(UUID contactId);
}
