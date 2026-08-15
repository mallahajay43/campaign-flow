package me.mallahajay43.campaignflow.audience.service;

import me.mallahajay43.campaignflow.audience.dto.response.ContactImportResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ContactImportService {
    ContactImportResponse findById(UUID importId);

    ContactImportResponse create(MultipartFile file);
}
