package me.mallahajay43.campaignflow.common.config;

import lombok.Getter;
import lombok.Setter;
import me.mallahajay43.campaignflow.common.enums.EventAggregateType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app.kafka")
@Getter
@Setter
public class KafkaAppProperties {

    private Map<String, String> topics = new HashMap<>();

    public String topicFor(EventAggregateType aggregateType) {
        String topic = topics.get(aggregateType.name().toLowerCase());
        if (topic == null) {
            throw new IllegalStateException("No Kafka topic is configured for aggregateType: "+aggregateType);
        }
        return topic;
    }

}
