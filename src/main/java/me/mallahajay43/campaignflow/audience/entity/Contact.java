package me.mallahajay43.campaignflow.audience.entity;

import jakarta.persistence.*;
import lombok.*;
import me.mallahajay43.campaignflow.common.entity.BaseEntity;
import me.mallahajay43.campaignflow.common.enums.ContactStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(length = 200)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private ContactStatus status = ContactStatus.ACTIVE;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> attributes;
}
