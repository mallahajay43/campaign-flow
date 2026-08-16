package me.mallahajay43.campaignflow.template.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.audience.service.Impl.ContactImportProcessor;
import me.mallahajay43.campaignflow.common.enums.ProcessingResult;
import me.mallahajay43.campaignflow.template.service.CampaignProcessor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CampaignConsumer {

    private final CampaignProcessor campaignProcessor;

    @KafkaListener(
            topics = "${app.kafka.topics.campaign_started:campaign.started}",
            groupId = "campaign-workers",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, Map<String, Object>> record, Acknowledgment ack) {

        Map<String, Object> envelope = record.value();
        Map<String, Object> data = (Map<String, Object>) envelope.get("data");
        String eventType = (String) envelope.get("eventType");

        Object tenantIdRaw = data.get("tenantId");
        Object campaignIdRaw = data.get("campaignId");
        if (tenantIdRaw == null || campaignIdRaw == null) {
            log.warn("No tenantId or campaignId was found, skipping event: {}", eventType);
            ack.acknowledge();
            return;
        }

        UUID tenantId = UUID.fromString(tenantIdRaw.toString());
        UUID campaignId = UUID.fromString(campaignIdRaw.toString());

        log.info("Starting campaign. tenantId={}, campaignId={}", tenantId, campaignId);

        campaignProcessor.process(tenantId, campaignId);
        ack.acknowledge();
    }
}
