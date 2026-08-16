package me.mallahajay43.campaignflow.common.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.common.entity.OutboxEvent;
import me.mallahajay43.campaignflow.common.config.KafkaAppProperties;
import me.mallahajay43.campaignflow.common.enums.OutboxStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaAppProperties kafkaProperties;
    private final OutboxResultHandler outboxResultHandler;

    @Scheduled(fixedDelay = 1000)
    public void poll() {

        List<OutboxEvent> pendingEvents = outboxEventRepository
                .findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        for (OutboxEvent event : pendingEvents) {
            try {
                String topic = kafkaProperties.topicFor(event.getAggregateType());
                String key = extractTenantId(event.getPayload());

                Map<String, Object> envelope = Map.of(
                        "eventType", event.getEventType(),
                        "aggregateType", event.getAggregateType().name(),
                        "aggregateId", event.getAggregateId().toString(),
                        "data", event.getPayload()
                );

                kafkaTemplate.send(topic, key, envelope)
                        .get(5, TimeUnit.SECONDS);

                outboxResultHandler.handleEventPublished(event);
            } catch (Exception e) {
                log.error("Outbox event failed, eventId: {}, attempts: {}", event.getId(), event.getAttempts());
                outboxResultHandler.handleEventFailed(event, e.getMessage());
            }
        }

    }


    private String extractTenantId(Map<String, Object> payload) {
        Object value = payload.get("tenantId");
        return value != null ? value.toString() : "unknown";
    }
}

