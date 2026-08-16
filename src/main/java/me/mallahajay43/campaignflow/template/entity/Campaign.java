package me.mallahajay43.campaignflow.template.entity;

import jakarta.persistence.*;
import lombok.*;
import me.mallahajay43.campaignflow.common.enums.CampaignAudienceType;
import me.mallahajay43.campaignflow.common.enums.CampaignStatus;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "campaigns")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 255)
    private String subject;

    @Column(name = "template_id", nullable = false)
    private UUID templateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "audience_type", nullable = false)
    private CampaignAudienceType audienceType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampaignStatus status;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Version
    private Long version;
}