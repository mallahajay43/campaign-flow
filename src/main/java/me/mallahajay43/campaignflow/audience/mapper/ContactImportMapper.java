package me.mallahajay43.campaignflow.audience.mapper;

import me.mallahajay43.campaignflow.audience.dto.request.CreateTagRequest;
import me.mallahajay43.campaignflow.audience.dto.response.ContactImportResponse;
import me.mallahajay43.campaignflow.audience.dto.response.TagResponse;
import me.mallahajay43.campaignflow.audience.entity.ContactImport;
import me.mallahajay43.campaignflow.audience.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContactImportMapper {
    ContactImportResponse toResponse(ContactImport contactImport);
}
