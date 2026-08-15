package me.mallahajay43.campaignflow.identity.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import me.mallahajay43.campaignflow.common.entity.BaseEntity;
import me.mallahajay43.campaignflow.common.enums.TenantStatus;
import me.mallahajay43.campaignflow.common.enums.UserRole;
import me.mallahajay43.campaignflow.common.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_users_tenant_email",
                columnNames = {"tenant_id", "email"}
        )
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false, updatable = false)
    private Tenant tenant;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserStatus status;

    @Version
    @Column(nullable = false)
    private long version;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public @Nullable String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
