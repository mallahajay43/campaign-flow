package me.mallahajay43.campaignflow.audience.repository;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.audience.entity.Contact;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ContactBatchRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String UPSERT_SQL = """
    insert into contacts (
        id,
        tenant_id,
        email,
        full_name,
        status,
        attributes,
        created_at,
        updated_at
    )
    values (
        :id,
        :tenantId,
        :email,
        :fullName,
        :status,
        cast(:attributes as jsonb),
        now(),
        now()
    )
    on conflict (tenant_id, email)
    do update set
        full_name = excluded.full_name,
        status = excluded.status,
        attributes = excluded.attributes,
        updated_at = now()
    """;

    private final ObjectMapper objectMapper;

    public void upsertContacts(UUID tenantId, List<Contact> contacts) {
        SqlParameterSource[] parameters = contacts.stream()
                .map(contact ->
                        new MapSqlParameterSource()
                                .addValue("id",
                                        contact.getId() != null
                                                ? contact.getId()
                                                : UUID.randomUUID(),
                                        Types.OTHER
                                )
                                .addValue(
                                        "tenantId",
                                        tenantId,
                                        Types.OTHER
                                )
                                .addValue("email", contact.getEmail())
                                .addValue(
                                        "fullName",
                                        contact.getFullName()
                                )
                                .addValue(
                                        "status",
                                        contact.getStatus().name()
                                )
                                .addValue("attributes", writeJson(contact.getAttributes()))
                ).toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(UPSERT_SQL, parameters);
    }

    private String writeJson(Map<String, Object> attributes) {
        return objectMapper.writeValueAsString(
                attributes == null
                        ? Map.of()
                        : attributes
        );
    }
}
