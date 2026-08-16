package me.mallahajay43.campaignflow.template.entity;

import jakarta.persistence.*;
import lombok.*;
import me.mallahajay43.campaignflow.common.enums.CampaignAudienceType;
import me.mallahajay43.campaignflow.common.enums.CampaignStatus;
import me.mallahajay43.campaignflow.common.enums.RecipientStatus;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "campaign_recipients",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_campaign_contact",
                columnNames = {"campaign_id", "contact_id"}
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "campaign_id", nullable = false)
    private UUID campaignId;

    @Column(name = "contact_id", nullable = false)
    private UUID contactId;

    @Column(nullable = false)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipientStatus status;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "failed_at")
    private Instant failedAt;

    @Column(name = "last_error")
    private String lastError;
}