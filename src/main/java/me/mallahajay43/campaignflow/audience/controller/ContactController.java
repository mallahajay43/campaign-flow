package me.mallahajay43.campaignflow.audience.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.audience.dto.request.CreateContactRequest;
import me.mallahajay43.campaignflow.audience.dto.request.UpdateContactRequest;
import me.mallahajay43.campaignflow.audience.dto.response.ContactResponse;
import me.mallahajay43.campaignflow.audience.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody CreateContactRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<ContactResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(contactService.findAll(pageable));
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<ContactResponse> findById(@PathVariable UUID contactId) {
        return ResponseEntity.ok(contactService.findById(contactId));
    }

    @PatchMapping("/{contactId}")
    public ResponseEntity<ContactResponse> update(@PathVariable UUID contactId, @Valid @RequestBody UpdateContactRequest request) {
        return ResponseEntity.ok(contactService.update(contactId, request));
    }

    @DeleteMapping("/{contactId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID contactId) {
        contactService.delete(contactId);
    }
}
