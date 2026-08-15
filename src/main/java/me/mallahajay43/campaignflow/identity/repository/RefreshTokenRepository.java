package me.mallahajay43.campaignflow.identity.repository;

import me.mallahajay43.campaignflow.identity.entity.RefreshToken;
import me.mallahajay43.campaignflow.identity.entity.Tenant;
import me.mallahajay43.campaignflow.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser_Id(UUID userId);
    void deleteByUser(User user);
    void deleteByToken(String token);
}
