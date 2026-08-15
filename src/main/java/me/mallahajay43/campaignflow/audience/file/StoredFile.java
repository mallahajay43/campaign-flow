package me.mallahajay43.campaignflow.audience.file;

public record StoredFile(
        String bucket,
        String objectKey,
        long size,
        String contentType
) {
}
