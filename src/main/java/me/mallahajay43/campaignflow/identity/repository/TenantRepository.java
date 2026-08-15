package me.mallahajay43.campaignflow.identity.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import me.mallahajay43.campaignflow.identity.entity.Tenant;
import me.mallahajay43.campaignflow.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    Boolean existsByEmail(String email);

    Optional<Tenant> findByEmail(String email);
}
