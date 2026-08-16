package me.mallahajay43.campaignflow.template.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private static final Duration TTL = Duration.ofHours(24);

    private final StringRedisTemplate redisTemplate;

    public boolean acquire(UUID tenantId, UUID campaignId, String idempotencyKey) {

        String key = key(tenantId, campaignId, idempotencyKey);

        Boolean acquired = redisTemplate.opsForValue()
                        .setIfAbsent(key, "PROCESSING", TTL);

        return Boolean.TRUE.equals(acquired);
    }

    public void release(UUID tenantId, UUID campaignId, String idempotencyKey) {
        redisTemplate.delete(key(tenantId, campaignId, idempotencyKey));
    }

    private String key(UUID tenantId, UUID campaignId, String idempotencyKey) {
        return "idempotency:%s:campaign:%s:%s"
                .formatted(tenantId, campaignId, idempotencyKey);
    }
}