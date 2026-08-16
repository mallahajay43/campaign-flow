package me.mallahajay43.campaignflow.template.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.template.dto.request.CreateCampaignRequest;
import me.mallahajay43.campaignflow.template.dto.response.CampaignResponse;
import me.mallahajay43.campaignflow.template.dto.response.CampaignStatsResponse;
import me.mallahajay43.campaignflow.template.service.CampaignSendService;
import me.mallahajay43.campaignflow.template.service.CampaignService;
import me.mallahajay43.campaignflow.template.service.CampaignStatsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService service;
    private final CampaignSendService campaignSendService;
    private final CampaignStatsService campaignStatsService;

    @PostMapping
    public ResponseEntity<CampaignResponse> create(@Valid @RequestBody CreateCampaignRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<CampaignResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<CampaignResponse> findById(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(service.findById(campaignId));
    }

    @PostMapping("/{campaignId}/send")
    public ResponseEntity<CampaignResponse> send(
            @PathVariable UUID campaignId,
            @RequestHeader("X-Idempotency-Key") String idempotencyKey
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(campaignSendService.send(campaignId, idempotencyKey));
    }

    @GetMapping("/{campaignId}/stats")
    public ResponseEntity<CampaignStatsResponse> stats(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(campaignStatsService.get(campaignId));
    }
}
