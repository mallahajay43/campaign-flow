package me.mallahajay43.campaignflow.identity.service;

import jakarta.validation.Valid;
import me.mallahajay43.campaignflow.identity.dto.request.UpdateTenantRequest;
import me.mallahajay43.campaignflow.identity.dto.response.TenantResponse;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface TenantService {
    TenantResponse getCurrentTenant(UUID tenantId);

    TenantResponse updateCurrentTenant(@Valid UpdateTenantRequest request, UUID tenantId);
}
