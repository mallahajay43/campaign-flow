package me.mallahajay43.campaignflow.template.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.template.dto.request.CreateEmailTemplateRequest;
import me.mallahajay43.campaignflow.template.dto.request.UpdateEmailTemplateRequest;
import me.mallahajay43.campaignflow.template.dto.response.EmailTemplateResponse;
import me.mallahajay43.campaignflow.template.service.EmailTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class EmailTemplateController {

    private final EmailTemplateService service;

    @PostMapping
    public ResponseEntity<EmailTemplateResponse> create(
            @Valid @RequestBody CreateEmailTemplateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<EmailTemplateResponse>> findAll(Pageable pageable) {

        return ResponseEntity.ok(
                service.findAll(pageable)
        );
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<EmailTemplateResponse> findById(@PathVariable UUID templateId) {
        return ResponseEntity.ok(
                service.findById(templateId)
        );
    }

    @PatchMapping("/{templateId}")
    public ResponseEntity<EmailTemplateResponse> update(
            @PathVariable UUID templateId,
            @Valid @RequestBody UpdateEmailTemplateRequest request
    ) {

        return ResponseEntity.ok(
                service.update(templateId, request));
    }

    @DeleteMapping("/{templateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID templateId) {
        service.delete(templateId);
    }
}
