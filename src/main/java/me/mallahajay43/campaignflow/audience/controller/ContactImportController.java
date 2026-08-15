package me.mallahajay43.campaignflow.audience.controller;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.audience.dto.response.ContactImportResponse;
import me.mallahajay43.campaignflow.audience.service.ContactImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contact-imports")
@RequiredArgsConstructor
public class ContactImportController {

    private final ContactImportService contactImportService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ContactImportResponse> create(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(contactImportService.create(file));
    }

    @GetMapping("/{importId}")
    public ResponseEntity<ContactImportResponse> status(@PathVariable UUID importId) {
        return ResponseEntity.ok(
                contactImportService.findById(importId)
        );
    }
}
