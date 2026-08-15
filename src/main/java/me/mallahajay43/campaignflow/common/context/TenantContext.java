package me.mallahajay43.campaignflow.common.context;

import lombok.Getter;
import lombok.Setter;
import me.mallahajay43.campaignflow.common.enums.UserRole;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@Component
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
public class TenantContext {
    private UUID userId;
    private UUID tenantId;
    private UserRole role;
}
