package me.mallahajay43.campaignflow.common.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
@RequiredArgsConstructor
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

//    private final MerchantContext merchantContext;

    @Override
    public Optional<String> getCurrentAuditor() {

//        try {
//            if (merchantContext instanceof MerchantContext) {
//                if (merchantContext.getApiKey() != null) {
//                    return Optional.of(merchantContext.getApiKey());
//                } else if (merchantContext.getMerchantId() != null) {
//                    return Optional.of(String.valueOf("merchant_id:" + merchantContext.getMerchantId()));
//                }
//            }
//        }
//        catch (Exception e) {
//            log.warn("Audit warning: {}", e.getMessage());
//        }

        return Optional.of("SYSTEM");
    }
}
