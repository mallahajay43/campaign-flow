package me.mallahajay43.campaignflow.common.config;

import lombok.Getter;
import lombok.Setter;
import me.mallahajay43.campaignflow.common.enums.EventAggregateType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "app.minio")
public record MinioProperties(
        String endpoint,
        String accessKey,
        String secretKey,
        String bucket
) {
}
