package me.mallahajay43.campaignflow.identity.service.impl;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import me.mallahajay43.campaignflow.identity.api.IdentityService;
import me.mallahajay43.campaignflow.identity.entity.Tenant;
import me.mallahajay43.campaignflow.identity.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdentityApiServiceImpl implements IdentityService {

    private final TenantRepository tenantRepository;

    @Override
    public String getTenantEmail(UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(
                () -> new ResourceNotFoundException("TENANT",  tenantId)
        );

        return tenant.getEmail();
    }
}
