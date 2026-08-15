package me.mallahajay43.campaignflow.audience.service;

import jakarta.validation.Valid;
import me.mallahajay43.campaignflow.audience.dto.request.CreateTagRequest;
import me.mallahajay43.campaignflow.audience.dto.response.TagResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface TagService {
    TagResponse create(@Valid CreateTagRequest request);

    List<TagResponse> findAll();

    void delete(UUID tagId);

    void assignTag(UUID contactId, UUID tagId);

    void removeTag(UUID contactId, UUID tagId);
}
