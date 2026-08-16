package me.mallahajay43.campaignflow.identity.api;

import java.util.Optional;
import java.util.UUID;

public interface IdentityService {
    String getTenantEmail(UUID tenantId);
}
