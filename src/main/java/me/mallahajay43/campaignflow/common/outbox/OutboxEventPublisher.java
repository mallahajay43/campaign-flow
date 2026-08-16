package me.mallahajay43.campaignflow.common.outbox;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.entity.OutboxEvent;
import me.mallahajay43.campaignflow.common.enums.EventAggregateType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {

    private final OutboxEventRepository outboxEventRepository;

    public void publish(EventAggregateType aggregateType, UUID aggregateId, String eventType,
                        Map<String, Object> payload) {
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .eventType(eventType)
                .payload(payload)
                .build();
        outboxEventRepository.save(outboxEvent);
    }
}
