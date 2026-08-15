package me.mallahajay43.campaignflow.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import me.mallahajay43.campaignflow.common.entity.BaseEntity;
import me.mallahajay43.campaignflow.common.enums.TenantStatus;

import java.util.UUID;

@Entity
@Table(name = "tenants")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Tenant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String tenantName;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TenantStatus status;

    @Column(nullable = false, length = 60)
    @Builder.Default
    private String timezone = "UTC";

    @Version
    @Column(nullable = false)
    private long version;
}
