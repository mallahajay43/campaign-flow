package me.mallahajay43.campaignflow.identity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.identity.dto.request.UpdateTenantRequest;
import me.mallahajay43.campaignflow.identity.dto.response.TenantResponse;
import me.mallahajay43.campaignflow.identity.service.TenantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tenants")
public class TenantController {

    private final TenantService tenantService;
    private final TenantContext tenantContext;

    @GetMapping("/current")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TenantResponse> getCurrentTenant() {
        return ResponseEntity.ok(tenantService.getCurrentTenant(tenantContext.getTenantId()));
    }

    @PatchMapping("/current")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TenantResponse> updateCurrentTenant(
            @Valid @RequestBody UpdateTenantRequest request) {
        return ResponseEntity.ok(tenantService.updateCurrentTenant(request, tenantContext.getTenantId()));
    }

}
