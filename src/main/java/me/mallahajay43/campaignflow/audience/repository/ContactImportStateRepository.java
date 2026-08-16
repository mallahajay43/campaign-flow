package me.mallahajay43.campaignflow.audience.repository;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.audience.dto.response.ContactImportEvent;
import me.mallahajay43.campaignflow.audience.entity.ContactImport;
import me.mallahajay43.campaignflow.audience.mapper.ContactImportMapper;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ContactImportStateRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ContactImportRepository contactImportRepository;
    private final ContactImportMapper mapper;

    public boolean claim(UUID tenantId, UUID importId, UUID workerId, Instant leaseUntil) {
        String sql = """
            update contact_imports
               set status = 'PROCESSING',
                   lease_owner = :workerId,
                   lease_until = :leaseUntil,
                   updated_at = now()
             where id = :importId
               and tenant_id = :tenantId
               and status <> 'COMPLETED'
               and (
                    lease_until is null
                    or lease_until < now()
                    or lease_owner = :workerId
               )
            """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                        .addValue("tenantId", tenantId)
                        .addValue("importId", importId)
                        .addValue("workerId", workerId)
                        .addValue("leaseUntil", Timestamp.from(leaseUntil));

        return jdbcTemplate.update(sql, parameters) == 1;
    }

    public ContactImportEvent find(UUID tenantId, UUID importId) {
        ContactImport importJob = contactImportRepository.findByIdAndTenantId(importId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("CONTACT_IMPORT", importId));

        return mapper.toEventResponse(importJob);
    }
}