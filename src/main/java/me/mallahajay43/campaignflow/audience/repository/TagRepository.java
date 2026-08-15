package me.mallahajay43.campaignflow.audience.repository;

import me.mallahajay43.campaignflow.audience.entity.Contact;
import me.mallahajay43.campaignflow.audience.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByIdAndTenantId(UUID id, UUID tenantId);

    List<Tag> findAllByTenantId(UUID tenantId);

    boolean existsByNameIgnoreCaseAndTenantId(String name, UUID tenantId);
}
