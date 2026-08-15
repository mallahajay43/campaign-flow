package me.mallahajay43.campaignflow.identity.repository;

import me.mallahajay43.campaignflow.identity.entity.Tenant;
import me.mallahajay43.campaignflow.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    List<User> findByTenant_Id(UUID tenantId);

    Optional<User> findByIdAndTenant_Id(UUID id, UUID tenantId);
}
