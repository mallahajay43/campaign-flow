package me.mallahajay43.campaignflow.audience.mapper;

import me.mallahajay43.campaignflow.audience.dto.request.CreateSegmentRequest;
import me.mallahajay43.campaignflow.audience.dto.request.CreateTagRequest;
import me.mallahajay43.campaignflow.audience.dto.response.SegmentResponse;
import me.mallahajay43.campaignflow.audience.dto.response.TagResponse;
import me.mallahajay43.campaignflow.audience.entity.Segment;
import me.mallahajay43.campaignflow.audience.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SegmentMapper {
    Segment toEntityFromCreateSegmentRequest(CreateSegmentRequest request);
    SegmentResponse toResponse(Segment segment);
    List<SegmentResponse> toResponseList(List<Segment> segmentList);
}
