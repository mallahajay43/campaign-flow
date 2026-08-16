package me.mallahajay43.campaignflow.audience.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.audience.dto.response.ContactImportEvent;
import me.mallahajay43.campaignflow.audience.file.MinioFileStorage;
import me.mallahajay43.campaignflow.audience.repository.ContactImportStateRepository;
import me.mallahajay43.campaignflow.common.enums.ProcessingResult;
import me.mallahajay43.campaignflow.common.enums.ImportStatus;
import me.mallahajay43.campaignflow.common.exceptions.InvalidCsvStructureException;
import me.mallahajay43.campaignflow.common.exceptions.InvalidImportEventException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactImportProcessor {

    private final ContactImportStateRepository stateRepository;
    private final ContactImportChunkService chunkService;
    private final MinioFileStorage fileStorage;
    private final ContactOpenCsvParser csvParser;

    @Value("${app.contact-import.batch-size:500}")
    private int batchSize;

    @Value("${app.contact-import.lease-duration:45s}")
    private Duration leaseDuration;

    /*
     * Stable for the lifetime of this application instance.
     */
    private final UUID workerId = UUID.randomUUID();

    public ProcessingResult process(UUID tenantId, UUID importId) {
        ContactImportEvent initialState = stateRepository.find(tenantId, importId);

        if (initialState.status() == ImportStatus.COMPLETED) {
            return ProcessingResult.ALREADY_COMPLETED;
        }

        boolean claimed = stateRepository.claim(tenantId, importId, workerId,
                Instant.now().plus(leaseDuration));

        if (!claimed) {
            throw new ConcurrentModificationException("Import claimed by other worker");
        }

        ContactImportEvent claimedImport = stateRepository.find(tenantId, importId);
        try (InputStream inputStream = fileStorage.download(claimedImport.objectKey())) {

            csvParser.parse(
                    inputStream,
                    claimedImport.processedRows(),
                    batchSize,
                    chunk -> chunkService.processChunk(
                            tenantId,
                            importId,
                            workerId,
                            leaseDuration,
                            chunk
                    )
            );

            chunkService.markCompleted(tenantId, importId, workerId);

            return ProcessingResult.COMPLETED;

        } catch (InvalidCsvStructureException exception) {

            throw exception;

        } catch (IOException exception) {
            throw new InvalidImportEventException("Import failed: " + importId, exception
            );
        }
    }
}