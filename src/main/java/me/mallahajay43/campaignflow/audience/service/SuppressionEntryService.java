package me.mallahajay43.campaignflow.audience.service;

import jakarta.validation.Valid;
import me.mallahajay43.campaignflow.audience.dto.request.CreateSuppressionRequest;
import me.mallahajay43.campaignflow.audience.dto.response.SuppressionResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface SuppressionEntryService {
    SuppressionResponse create(CreateSuppressionRequest request);

    List<SuppressionResponse> findAll();

    void delete(UUID suppressionId);
}
