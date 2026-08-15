package me.mallahajay43.campaignflow.audience.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import me.mallahajay43.campaignflow.common.entity.BaseEntity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "contact_import_files")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactImportFile extends BaseEntity {

    @Id
    @Column(name = "import_id")
    private UUID importId;

    @Column(nullable = false, columnDefinition = "bytea")
    private byte[] content;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "size_bytes", nullable = false)
    private long sizeBytes;
}
