package me.mallahajay43.campaignflow.audience.entity;

import jakarta.persistence.*;
import lombok.*;
import me.mallahajay43.campaignflow.common.entity.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "contact_tags")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "contact_id", nullable = false)
    private UUID contactId;

    @Column(name = "tag_id", nullable = false)
    private UUID tagId;
}
