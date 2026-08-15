package me.mallahajay43.campaignflow.audience.service.Impl;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.audience.dto.response.ContactImportResponse;
import me.mallahajay43.campaignflow.audience.entity.ContactImport;
import me.mallahajay43.campaignflow.audience.file.StoredFile;
import me.mallahajay43.campaignflow.audience.mapper.ContactImportMapper;
import me.mallahajay43.campaignflow.audience.outbox.OutboxEventPublisher;
import me.mallahajay43.campaignflow.audience.repository.ContactImportRepository;
import me.mallahajay43.campaignflow.common.enums.EventAggregateType;
import me.mallahajay43.campaignflow.common.enums.ImportStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactImportTransactionalService {

    private final ContactImportRepository repository;
    private final OutboxEventPublisher outboxEventPublisher;
    private final ContactImportMapper contactImportMapper;

    @Transactional
    public ContactImportResponse create(UUID importId, UUID tenantId, String fileName, StoredFile storedFile) {

        ContactImport importJob =
                ContactImport.builder()
                        .id(importId)
                        .tenantId(tenantId)
                        .fileName(fileName)
                        .bucketName(storedFile.bucket())
                        .objectKey(storedFile.objectKey())
                        .fileSize(storedFile.size())
                        .status(ImportStatus.PENDING)
                        .build();
        repository.save(importJob);

        outboxEventPublisher.publish(EventAggregateType.CONTACT_IMPORT_REQUESTED, importId, "CONTACT_IMPORT",
                Map.of("importId", importId.toString(),
                        "tenantId", tenantId.toString(),
                        "importStatus", ImportStatus.PENDING.name(),
                        "occurredAt", Instant.now().toString()
                )
        );

        return contactImportMapper.toResponse(importJob);
    }
}