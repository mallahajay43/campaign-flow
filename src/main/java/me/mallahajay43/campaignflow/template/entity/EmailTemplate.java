package me.mallahajay43.campaignflow.template.entity;

import jakarta.persistence.*;
import lombok.*;
import me.mallahajay43.campaignflow.common.entity.BaseEntity;
import me.mallahajay43.campaignflow.common.enums.TemplateStatus;

import java.util.UUID;

@Entity
@Table(name = "email_templates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 255)
    private String subject;

    @Column(name = "html_content", nullable = false, columnDefinition = "TEXT")
    private String htmlContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TemplateStatus status;
}