package me.mallahajay43.campaignflow.audience.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.audience.dto.response.ContactImportResponse;
import me.mallahajay43.campaignflow.audience.entity.ContactImport;
import me.mallahajay43.campaignflow.audience.file.FileStorage;
import me.mallahajay43.campaignflow.audience.file.StoredFile;
import me.mallahajay43.campaignflow.audience.mapper.ContactImportMapper;
import me.mallahajay43.campaignflow.audience.repository.ContactImportRepository;
import me.mallahajay43.campaignflow.audience.service.ContactImportService;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.common.exceptions.FileImportException;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactImportServiceImpl implements ContactImportService{

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final FileStorage fileStorage;
    private final ContactImportRepository repository;
    private final ContactImportTransactionalService transactionalService;
    private final TenantContext tenantContext;
    private final ContactImportMapper mapper;

    public ContactImportResponse create(MultipartFile file) {

        validate(file);

        UUID tenantId = tenantContext.getTenantId();

        UUID importId = UUID.randomUUID();
        String safeFileName = sanitize(file.getOriginalFilename());
        String objectKey = String.format(
                "contact-imports/%s/%s/%s",
                tenantId,
                importId,
                safeFileName
        );

        StoredFile storedFile = fileStorage.upload(objectKey, file);

        try {
            return transactionalService.create(importId, tenantId, safeFileName, storedFile);
        } catch (Exception exception) {
            try {
                // Safe File deletion on failed create.
                fileStorage.delete(storedFile.bucket(), storedFile.objectKey());
            } catch (Exception cleanupException) {
                log.error("FILE_IMPORT: Failed to delete file {}", safeFileName);
            }
            throw exception;
        }
    }

    @Transactional(readOnly = true)
    public ContactImportResponse findById(UUID importId) {

        UUID tenantId = tenantContext.getTenantId();

        ContactImport importJob = repository.findByIdAndTenantId(importId, tenantId)
                        .orElseThrow(() -> new ResourceNotFoundException("CONTACT_IMPORT", importId));

        return mapper.toResponse(importJob);
    }

    private void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new FileImportException("CONTACT_IMPORT", "File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileImportException("CONTACT_IMPORT", "CSV file exceeds 10 MB");
        }

        String filename = file.getOriginalFilename();

        if (filename == null || !filename.toLowerCase(Locale.ROOT).endsWith(".csv")) {
            throw new FileImportException("CONTACT_IMPORT", "Only CSV files are supported");
        }
    }

    private String sanitize(String filename) {

        if (filename == null) {
            return "contacts.csv";
        }

        return Paths.get(filename)
                .getFileName()
                .toString()
                .replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
