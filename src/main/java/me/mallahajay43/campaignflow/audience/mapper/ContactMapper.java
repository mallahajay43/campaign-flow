package me.mallahajay43.campaignflow.audience.mapper;

import me.mallahajay43.campaignflow.audience.api.ContactProjection;
import me.mallahajay43.campaignflow.audience.dto.request.CreateContactRequest;
import me.mallahajay43.campaignflow.audience.dto.request.UpdateContactRequest;
import me.mallahajay43.campaignflow.audience.dto.response.ContactResponse;
import me.mallahajay43.campaignflow.audience.entity.Contact;
import me.mallahajay43.campaignflow.identity.dto.request.UpdateUserRequest;
import me.mallahajay43.campaignflow.identity.dto.request.UserCreateRequest;
import me.mallahajay43.campaignflow.identity.dto.response.UserResponse;
import me.mallahajay43.campaignflow.identity.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContactMapper {
    Contact toEntityFromCreateContactRequest(CreateContactRequest request);
    ContactResponse toResponse(Contact contact);
    List<ContactResponse> toResponseList(List<Contact> contacts);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRecord(UpdateContactRequest request, @MappingTarget Contact contact);

    ContactProjection toProjectionEntity(Contact contact);
}
