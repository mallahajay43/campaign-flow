package me.mallahajay43.campaignflow.audience.file;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.config.MinioProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MinioBucketInitializer {

    private final MinioClient minioClient;
    private final MinioProperties properties;

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        try {
            boolean exists = minioClient
                    .bucketExists(
                            BucketExistsArgs.builder()
                                    .bucket(properties.bucket())
                                    .build()
                    );

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(properties.bucket())
                                .build()
                );
            }

        } catch (Exception exception) {
            throw new IllegalStateException("Unable to initialize MinIO bucket", exception);
        }
    }
}
