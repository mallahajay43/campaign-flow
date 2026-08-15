package me.mallahajay43.campaignflow.identity.mapper;

import me.mallahajay43.campaignflow.identity.dto.request.TenantSignupRequest;
import me.mallahajay43.campaignflow.identity.dto.request.UpdateTenantRequest;
import me.mallahajay43.campaignflow.identity.dto.response.TenantResponse;
import me.mallahajay43.campaignflow.identity.entity.Tenant;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TenantMapper {
    Tenant toEntityFromTenantSignupRequest(TenantSignupRequest request);
    TenantResponse toResponse(Tenant tenant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRecord(UpdateTenantRequest request, @MappingTarget Tenant tenant);
}
