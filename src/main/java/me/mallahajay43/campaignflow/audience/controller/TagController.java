package me.mallahajay43.campaignflow.audience.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.audience.dto.request.CreateContactRequest;
import me.mallahajay43.campaignflow.audience.dto.request.CreateTagRequest;
import me.mallahajay43.campaignflow.audience.dto.request.UpdateContactRequest;
import me.mallahajay43.campaignflow.audience.dto.response.ContactResponse;
import me.mallahajay43.campaignflow.audience.dto.response.TagResponse;
import me.mallahajay43.campaignflow.audience.service.ContactService;
import me.mallahajay43.campaignflow.audience.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping("/tags")
    public ResponseEntity<TagResponse> create(@Valid @RequestBody CreateTagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagService.create(request));
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(tagService.findAll());
    }

    @DeleteMapping("/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID tagId) {
        tagService.delete(tagId);
    }

    @PostMapping("/contacts/{contactId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignTag(@PathVariable UUID contactId, @PathVariable UUID tagId) {
        tagService.assignTag(contactId, tagId);
    }

    @DeleteMapping("/contacts/{contactId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTag(@PathVariable UUID contactId, @PathVariable UUID tagId) {
        tagService.removeTag(contactId, tagId);
    }

}
