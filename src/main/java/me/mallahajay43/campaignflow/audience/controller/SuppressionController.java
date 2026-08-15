package me.mallahajay43.campaignflow.audience.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.audience.dto.request.CreateSuppressionRequest;
import me.mallahajay43.campaignflow.audience.dto.response.SuppressionResponse;
import me.mallahajay43.campaignflow.audience.service.SuppressionEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/suppressions")
@RequiredArgsConstructor
public class SuppressionController {

    private final SuppressionEntryService suppressionEntryService;

    @PostMapping
    public ResponseEntity<SuppressionResponse> create(@Valid @RequestBody CreateSuppressionRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(suppressionEntryService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<SuppressionResponse>> findAll() {
        return ResponseEntity.ok(
                suppressionEntryService.findAll()
        );
    }

    @DeleteMapping("/{suppressionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID suppressionId) {
        suppressionEntryService.delete(suppressionId);
    }
}
