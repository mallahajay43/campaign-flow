package me.mallahajay43.campaignflow.audience.service;

import jakarta.validation.Valid;
import me.mallahajay43.campaignflow.audience.dto.request.CreateSegmentRequest;
import me.mallahajay43.campaignflow.audience.dto.response.SegmentResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface SegmentService {
    SegmentResponse create(CreateSegmentRequest request);

    List<SegmentResponse> findAll();

    SegmentResponse findById(UUID segmentId);

    void delete(UUID segmentId);
}
