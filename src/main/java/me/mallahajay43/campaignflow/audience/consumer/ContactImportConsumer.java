package me.mallahajay43.campaignflow.audience.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.audience.service.Impl.ContactImportProcessor;
import me.mallahajay43.campaignflow.common.enums.ProcessingResult;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContactImportConsumer {

    private final ContactImportProcessor importProcessor;

    @KafkaListener(
            topics = "${app.kafka.topics.contact_import_requested:contact.import.requested}",
            groupId = "contact-import-workers",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, Map<String, Object>> record, Acknowledgment ack) {

        Map<String, Object> envelope = record.value();
        Map<String, Object> data = (Map<String, Object>) envelope.get("data");
        String eventType = (String) envelope.get("eventType");

        Object tenantIdRaw = data.get("tenantId");
        Object importIdRaw = data.get("importId");
        if (tenantIdRaw == null || importIdRaw == null) {
            log.warn("No tenantId or importId was found, skipping event: {}", eventType);
            ack.acknowledge();
            return;
        }

        UUID tenantId = UUID.fromString(tenantIdRaw.toString());
        UUID importId = UUID.fromString(importIdRaw.toString());

        log.info("Starting contact import. tenantId={}, importId={}", tenantId, importId);

        ProcessingResult result = importProcessor.process(tenantId, importId);

        if (result == ProcessingResult.COMPLETED ||
                result == ProcessingResult.ALREADY_COMPLETED) {

            ack.acknowledge();

            log.info("Contact import event acknowledged. tenantId={}, importId={}, result={}",
                    tenantId, importId, result.name());
        }
    }
}
