package me.mallahajay43.campaignflow.identity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.identity.dto.request.ChangeUserRoleRequest;
import me.mallahajay43.campaignflow.identity.dto.request.UpdateUserRequest;
import me.mallahajay43.campaignflow.identity.dto.request.UserCreateRequest;
import me.mallahajay43.campaignflow.identity.dto.response.UserResponse;
import me.mallahajay43.campaignflow.identity.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final TenantContext tenantContext;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> fetchUsers() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.fetchUsers(tenantContext.getTenantId()));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> fetchCurrentUser() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.fetchCurrentUser(tenantContext.getUserId(), tenantContext.getTenantId()));
    }

    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUser(tenantContext.getUserId(), updateUserRequest, tenantContext.getTenantId()));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> fetchUsers(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.fetchUser(userId, tenantContext.getTenantId()));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(tenantContext.getTenantId(), userCreateRequest));
    }

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> changeRole(
            @PathVariable UUID userId,
            @Valid @RequestBody ChangeUserRoleRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.changeRole(userId, request, tenantContext.getTenantId()));
    }

    @PostMapping("/{userId}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> suspend(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.suspend(userId, tenantContext.getTenantId()));
    }

    @PostMapping("/{userId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> activate(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.activate(userId, tenantContext.getTenantId()));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        userService.delete(userId, tenantContext.getTenantId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

}
