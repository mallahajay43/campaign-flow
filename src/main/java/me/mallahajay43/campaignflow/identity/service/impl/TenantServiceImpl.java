package me.mallahajay43.campaignflow.identity.service.impl;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.exceptions.DuplicateResourceException;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import me.mallahajay43.campaignflow.identity.dto.request.UpdateTenantRequest;
import me.mallahajay43.campaignflow.identity.dto.response.TenantResponse;
import me.mallahajay43.campaignflow.identity.entity.Tenant;
import me.mallahajay43.campaignflow.identity.mapper.TenantMapper;
import me.mallahajay43.campaignflow.identity.repository.TenantRepository;
import me.mallahajay43.campaignflow.identity.service.TenantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    @Override
    public TenantResponse getCurrentTenant(UUID tenantId) {
        return tenantMapper.toResponse(tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("TENANT", tenantId.toString())));
    }

    @Override
    @Transactional
    public TenantResponse updateCurrentTenant(UpdateTenantRequest request, UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("TENANT", tenantId.toString()));

        if (request.email() != null && !tenant.getEmail().equals(request.email())) {
            Optional<Tenant> tenantOptional = tenantRepository.findByEmail(request.email());
            if (tenantOptional.isPresent()) {
                throw new DuplicateResourceException("TENANT", "Tenant already exists with email: " + request.email());
            }
        }

        tenantMapper.updateEntityFromRecord(request,tenant);
        tenant = tenantRepository.save(tenant);

        return tenantMapper.toResponse(tenant);
    }
}
