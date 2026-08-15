package me.mallahajay43.campaignflow.identity.mapper;

import me.mallahajay43.campaignflow.identity.dto.request.TenantSignupRequest;
import me.mallahajay43.campaignflow.identity.dto.request.UpdateTenantRequest;
import me.mallahajay43.campaignflow.identity.dto.request.UpdateUserRequest;
import me.mallahajay43.campaignflow.identity.dto.request.UserCreateRequest;
import me.mallahajay43.campaignflow.identity.dto.response.TenantResponse;
import me.mallahajay43.campaignflow.identity.dto.response.UserResponse;
import me.mallahajay43.campaignflow.identity.entity.Tenant;
import me.mallahajay43.campaignflow.identity.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntityFromUserCreateRequest(UserCreateRequest request);
    UserResponse toResponse(User user);
    List<UserResponse> toResponseList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRecord(UpdateUserRequest request, @MappingTarget User user);
}
