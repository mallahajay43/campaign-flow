package me.mallahajay43.campaignflow.audience.entity;

import jakarta.persistence.*;
import lombok.*;
import me.mallahajay43.campaignflow.common.entity.BaseEntity;
import me.mallahajay43.campaignflow.common.enums.EventAggregateType;
import me.mallahajay43.campaignflow.common.enums.OutboxStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventAggregateType aggregateType;

    @Column(nullable = false)
    private UUID aggregateId;

    @Column(nullable = false, length = 50)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> payload;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private OutboxStatus status = OutboxStatus.PENDING;

    @Column(nullable = false)
    @Builder.Default
    private Integer attempts = 0;

    @Column(length = 1000)
    private String lastError;

    private LocalDateTime publishedAt;
}
