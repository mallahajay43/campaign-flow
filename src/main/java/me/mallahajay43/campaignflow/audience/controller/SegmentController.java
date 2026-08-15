package me.mallahajay43.campaignflow.audience.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.audience.dto.request.CreateSegmentRequest;
import me.mallahajay43.campaignflow.audience.dto.response.SegmentResponse;
import me.mallahajay43.campaignflow.audience.service.SegmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/segments")
@RequiredArgsConstructor
public class SegmentController {

    private final SegmentService segmentService;

    @PostMapping
    public ResponseEntity<SegmentResponse> create(@Valid @RequestBody CreateSegmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(segmentService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<SegmentResponse>> findAll() {
        return ResponseEntity.ok(segmentService.findAll());
    }

    @GetMapping("/{segmentId}")
    public ResponseEntity<SegmentResponse> findById(@PathVariable UUID segmentId) {
        return ResponseEntity.ok(segmentService.findById(segmentId));
    }

    @DeleteMapping("/{segmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID segmentId) {
        segmentService.delete(segmentId);
    }
}
