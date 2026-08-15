package me.mallahajay43.campaignflow.audience.entity;

import jakarta.persistence.*;
import lombok.*;
import me.mallahajay43.campaignflow.common.entity.BaseEntity;
import me.mallahajay43.campaignflow.common.enums.ImportStatus;
import me.mallahajay43.campaignflow.common.enums.SuppressionReason;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contact_imports")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactImport extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "bucket_name", nullable = false)
    private String bucketName;

    @Column(name = "object_key", nullable = false)
    private String objectKey;

    @Column(name = "file_size", nullable = false)
    private long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImportStatus status;

    @Column(name = "total_records", nullable = false)
    private long totalRecords;

    @Column(name = "processed_rows", nullable = false)
    private long processedRows;

    @Column(name = "success_count", nullable = false)
    private long successCount;

    @Column(name = "failed_count", nullable = false)
    private long failedCount;

    @Column(name = "skipped_count", nullable = false)
    private long skippedCount;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
