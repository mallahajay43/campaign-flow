package me.mallahajay43.campaignflow.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import me.mallahajay43.campaignflow.common.entity.BaseEntity;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_refresh_tokens_user_token",
                columnNames = {"user_id", "token"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 1000)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime lastUsedAt;
}
