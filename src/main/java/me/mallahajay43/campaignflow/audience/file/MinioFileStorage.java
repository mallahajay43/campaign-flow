package me.mallahajay43.campaignflow.audience.file;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.config.MinioProperties;
import me.mallahajay43.campaignflow.common.exceptions.FileStorageException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class MinioFileStorage implements FileStorage {

    private final MinioClient minioClient;
    private final MinioProperties properties;

    @Override
    public StoredFile upload(String objectKey, MultipartFile file) {

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.bucket())
                            .object(objectKey)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return new StoredFile(
                    properties.bucket(),
                    objectKey,
                    file.getSize(),
                    file.getContentType()
            );

        } catch (Exception exception) {
            throw new FileStorageException("Unable to upload file to object storage", exception);
        }
    }

    @Override
    public InputStream download(String bucket, String objectKey) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build()
            );

        } catch (Exception exception) {
            throw new FileStorageException("Unable to download file", exception);
        }
    }

    @Override
    public void delete(String bucket, String objectKey) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .build()
            );
        } catch (Exception exception) {
            throw new FileStorageException("Unable to delete file", exception);
        }
    }
}
