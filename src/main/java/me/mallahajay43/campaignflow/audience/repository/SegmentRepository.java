package me.mallahajay43.campaignflow.audience.repository;

import me.mallahajay43.campaignflow.audience.entity.Segment;
import me.mallahajay43.campaignflow.audience.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SegmentRepository extends JpaRepository<Segment, UUID> {
    Optional<Segment> findByIdAndTenantId(UUID id, UUID tenantId);

    List<Segment> findAllByTenantId(UUID tenantId);
}
