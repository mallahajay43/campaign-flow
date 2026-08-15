package me.mallahajay43.campaignflow.audience.mapper;

import me.mallahajay43.campaignflow.audience.dto.request.CreateContactRequest;
import me.mallahajay43.campaignflow.audience.dto.request.CreateTagRequest;
import me.mallahajay43.campaignflow.audience.dto.request.UpdateContactRequest;
import me.mallahajay43.campaignflow.audience.dto.response.ContactResponse;
import me.mallahajay43.campaignflow.audience.dto.response.TagResponse;
import me.mallahajay43.campaignflow.audience.entity.Contact;
import me.mallahajay43.campaignflow.audience.entity.Tag;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {
    Tag toEntityFromCreateTagRequest(CreateTagRequest request);
    TagResponse toResponse(Tag tag);
    List<TagResponse> toResponseList(List<Tag> tagList);
}
