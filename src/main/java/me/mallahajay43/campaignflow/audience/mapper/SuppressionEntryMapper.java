package me.mallahajay43.campaignflow.audience.mapper;

import me.mallahajay43.campaignflow.audience.dto.request.CreateSuppressionRequest;
import me.mallahajay43.campaignflow.audience.dto.request.CreateTagRequest;
import me.mallahajay43.campaignflow.audience.dto.response.SuppressionResponse;
import me.mallahajay43.campaignflow.audience.dto.response.TagResponse;
import me.mallahajay43.campaignflow.audience.entity.SuppressionEntry;
import me.mallahajay43.campaignflow.audience.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SuppressionEntryMapper {
    SuppressionEntry toEntityFromCreateSuppressionRequest(CreateSuppressionRequest request);
    SuppressionResponse toResponse(SuppressionEntry suppressionEntry);
    List<SuppressionResponse> toResponseList(List<SuppressionEntry> suppressionEntryList);
}
