package me.mallahajay43.campaignflow.common.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
@RequiredArgsConstructor
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    private final TenantContext tenantContext;

    @Override
    public Optional<String> getCurrentAuditor() {

        try {
            if (tenantContext != null) {
                if (tenantContext.getUserId() != null) {
                    return Optional.of(String.valueOf("user_id:" + tenantContext.getUserId()));
                }
            }
        }
        catch (Exception e) {
            log.warn("Audit warning: {}", e.getMessage());
        }

        return Optional.of("SYSTEM");
    }
}
