package me.mallahajay43.campaignflow.audience.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.audience.dto.request.CreateSegmentRequest;
import me.mallahajay43.campaignflow.audience.dto.response.SegmentResponse;
import me.mallahajay43.campaignflow.audience.entity.Segment;
import me.mallahajay43.campaignflow.audience.mapper.SegmentMapper;
import me.mallahajay43.campaignflow.audience.repository.SegmentRepository;
import me.mallahajay43.campaignflow.audience.service.SegmentService;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SegmentServiceImpl implements SegmentService {

    private final SegmentRepository segmentRepository;
    private final SegmentMapper segmentMapper;
    private final TenantContext tenantContext;

    @Override
    @Transactional
    public SegmentResponse create(CreateSegmentRequest request) {
        UUID tenantId = tenantContext.getTenantId();

        Segment segment = segmentMapper.toEntityFromCreateSegmentRequest(request);
        segment.setTenantId(tenantId);
        segment = segmentRepository.save(segment);
        return segmentMapper.toResponse(segment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SegmentResponse> findAll() {
        UUID tenantId = tenantContext.getTenantId();
        return segmentMapper.toResponseList(segmentRepository.findAllByTenantId(tenantId));
    }

    @Override
    @Transactional(readOnly = true)
    public SegmentResponse findById(UUID segmentId) {
        return segmentMapper.toResponse(getSegment(segmentId));
    }

    @Override
    @Transactional
    public void delete(UUID segmentId) {
        Segment segment = getSegment(segmentId);
        segmentRepository.delete(segment);
    }

    private Segment getSegment(UUID segmentId) {
        UUID tenantId = tenantContext.getTenantId();
        return segmentRepository.findByIdAndTenantId(segmentId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("SEGMENT", segmentId.toString()));
    }
}
