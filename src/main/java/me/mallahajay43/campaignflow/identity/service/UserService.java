package me.mallahajay43.campaignflow.identity.service;

import jakarta.validation.Valid;
import me.mallahajay43.campaignflow.identity.dto.request.ChangeUserRoleRequest;
import me.mallahajay43.campaignflow.identity.dto.request.UpdateUserRequest;
import me.mallahajay43.campaignflow.identity.dto.request.UserCreateRequest;
import me.mallahajay43.campaignflow.identity.dto.response.UserResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> fetchUsers(UUID tenantId);

    UserResponse fetchUser(UUID userId, UUID tenantId);

    UserResponse createUser(UUID tenantId, UserCreateRequest userCreateRequest);

    UserResponse changeRole(UUID userId, ChangeUserRoleRequest request, UUID tenantId);

    UserResponse suspend(UUID userId, UUID tenantId);

    UserResponse activate(UUID userId, UUID tenantId);

    void delete(UUID userId, UUID tenantId);

    UserResponse fetchCurrentUser(UUID userId, UUID tenantId);

    UserResponse updateUser(UUID userId, UpdateUserRequest updateUserRequest, UUID tenantId);
}
