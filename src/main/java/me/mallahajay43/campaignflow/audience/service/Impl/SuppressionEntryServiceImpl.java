package me.mallahajay43.campaignflow.audience.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.audience.dto.request.CreateSuppressionRequest;
import me.mallahajay43.campaignflow.audience.dto.request.CreateTagRequest;
import me.mallahajay43.campaignflow.audience.dto.response.SuppressionResponse;
import me.mallahajay43.campaignflow.audience.dto.response.TagResponse;
import me.mallahajay43.campaignflow.audience.entity.ContactTag;
import me.mallahajay43.campaignflow.audience.entity.SuppressionEntry;
import me.mallahajay43.campaignflow.audience.entity.Tag;
import me.mallahajay43.campaignflow.audience.mapper.SuppressionEntryMapper;
import me.mallahajay43.campaignflow.audience.mapper.TagMapper;
import me.mallahajay43.campaignflow.audience.repository.ContactRepository;
import me.mallahajay43.campaignflow.audience.repository.ContactTagRepository;
import me.mallahajay43.campaignflow.audience.repository.SuppressionEntryRepository;
import me.mallahajay43.campaignflow.audience.repository.TagRepository;
import me.mallahajay43.campaignflow.audience.service.SuppressionEntryService;
import me.mallahajay43.campaignflow.audience.service.TagService;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.common.enums.SuppressionSource;
import me.mallahajay43.campaignflow.common.exceptions.DuplicateResourceException;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SuppressionEntryServiceImpl implements SuppressionEntryService {

    private final SuppressionEntryRepository repository;
    private final SuppressionEntryMapper mapper;
    private final TenantContext tenantContext;

    @Override
    @Transactional
    public SuppressionResponse create(CreateSuppressionRequest request) {

        UUID tenantId = tenantContext.getTenantId();

        String email = request.email().trim().toLowerCase(Locale.ROOT);

        var existing = repository.findByTenantIdAndEmailIgnoreCase(tenantId, email);

        if (existing.isPresent()) {
            return mapper.toResponse(existing.get());
        }

        SuppressionEntry entry = mapper.toEntityFromCreateSuppressionRequest(request);
        entry.setTenantId(tenantId);
        entry.setEmail(email);
        entry.setSource(SuppressionSource.USER);

        entry = repository.save(entry);
        return mapper.toResponse(entry);
    }

    @Override
    public List<SuppressionResponse> findAll() {
        UUID tenantId = tenantContext.getTenantId();
        return mapper.toResponseList(repository.findAllByTenantId(tenantId));
    }

    @Override
    public void delete(UUID suppressionId) {
        UUID tenantId = tenantContext.getTenantId();
        SuppressionEntry entry = repository.findByIdAndTenantId(suppressionId, tenantId).orElseThrow(
                () -> new ResourceNotFoundException("SUPPRESSION_ENTRY", tenantId.toString()));

        repository.delete(entry);
    }
}
