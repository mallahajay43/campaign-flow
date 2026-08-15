package me.mallahajay43.campaignflow.audience.entity;

import jakarta.persistence.*;
import lombok.*;
import me.mallahajay43.campaignflow.common.entity.BaseEntity;
import me.mallahajay43.campaignflow.common.enums.SegmentType;
import me.mallahajay43.campaignflow.common.enums.SuppressionReason;
import me.mallahajay43.campaignflow.common.enums.SuppressionSource;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "suppression_entries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuppressionEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false, length = 320)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuppressionReason reason;

    @Enumerated(EnumType.STRING)
    private SuppressionSource source;

    @Column(name = "campaign_id")
    private UUID campaignId;
}
