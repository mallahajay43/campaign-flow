package me.mallahajay43.campaignflow.audience.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileStorage {

    StoredFile upload(
            String objectKey,
            MultipartFile file
    );

    InputStream download(String objectKey);

    void delete(String objectKey);
}
