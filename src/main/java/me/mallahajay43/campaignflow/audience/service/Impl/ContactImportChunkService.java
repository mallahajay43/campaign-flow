package me.mallahajay43.campaignflow.audience.service.Impl;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.audience.dto.ParsedContactImportRow;
import me.mallahajay43.campaignflow.audience.entity.Contact;
import me.mallahajay43.campaignflow.audience.repository.ContactBatchRepository;
import me.mallahajay43.campaignflow.common.exceptions.ImportLeaseLostException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactImportChunkService {

    private final ContactBatchRepository contactRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional
    public void processChunk(UUID tenantId, UUID importId, UUID workerId,
            Duration leaseDuration, List<ParsedContactImportRow> rows
    ) {

        // Verify for the lease.
        verifyLease(importId, workerId);

        List<Contact> validContacts = rows.stream()
                .filter(ParsedContactImportRow::valid)
                .map(ParsedContactImportRow::contact)
                .toList();

        if (!validContacts.isEmpty()) {
            contactRepository.upsertContacts(tenantId, validContacts);
        }

        long successfulRows = rows.stream()
                .filter(ParsedContactImportRow::valid)
                .count();

        long failedRows = rows.size() - successfulRows;

        long lastProcessedRow = rows.getLast()
                .rowNumber();

        updateCheckpoint(
                importId,
                workerId,
                lastProcessedRow,
                rows.size(),
                successfulRows,
                failedRows,
                Instant.now().plus(leaseDuration)
        );
    }

    private void verifyLease(UUID importId, UUID workerId) {
        String sql = """
            select count(*)
              from contact_imports
             where id = :importId
               and status = 'PROCESSING'
               and lease_owner = :workerId
               and lease_until > now()
            """;

        Integer count = jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource()
                        .addValue("importId", importId)
                        .addValue("workerId", workerId),
                Integer.class
        );

        if (count == null || count != 1) {
            throw new ImportLeaseLostException("No lease available for import: " + importId);
        }
    }

    private void updateCheckpoint(
            UUID importId,
            UUID workerId,
            long lastProcessedRow,
            long processedRows,
            long successfulRows,
            long failedRows,
            Instant leaseUntil
    ) {
        String sql = """
            update contact_imports
               set processed_rows =
                       processed_rows + :processedRows,
                   success_count =
                       success_count + :successfulRows,
                   failed_count =
                       failed_count + :failedRows,
                   lease_until = :leaseUntil,
                   updated_at = now()
             where id = :importId
               and status = 'PROCESSING'
               and lease_owner = :workerId
               and lease_until > now()
            """;

        int updated = jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("importId", importId)
                        .addValue("workerId", workerId)
                        .addValue("lastProcessedRow", lastProcessedRow)
                        .addValue("processedRows", processedRows)
                        .addValue("successfulRows", successfulRows)
                        .addValue("failedRows", failedRows)
                        .addValue("leaseUntil", Timestamp.from(leaseUntil))
        );

        if (updated != 1) {
            throw new ImportLeaseLostException("Import lease lost. " + importId);
        }
    }

    @Transactional
    public void markCompleted(UUID tenantId, UUID importId, UUID workerId) {
        String sql = """
        update contact_imports
           set status = 'COMPLETED',
               lease_owner = null,
               lease_until = null,
               updated_at = now()
         where id = :importId
           and tenant_id = :tenantId
           and status = 'PROCESSING'
           and lease_owner = :workerId
        """;

        int updated = jdbcTemplate.update(
                sql,
                new MapSqlParameterSource()
                        .addValue("tenantId", tenantId)
                        .addValue("importId", importId)
                        .addValue("workerId", workerId)
        );

        if (updated != 1) {
            throw new ImportLeaseLostException(
                    "Could not complete import: " + importId
            );
        }
    }
}