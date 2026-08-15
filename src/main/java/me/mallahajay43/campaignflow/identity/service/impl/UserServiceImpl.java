package me.mallahajay43.campaignflow.identity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.common.enums.UserStatus;
import me.mallahajay43.campaignflow.common.exceptions.DuplicateResourceException;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import me.mallahajay43.campaignflow.identity.dto.request.ChangeUserRoleRequest;
import me.mallahajay43.campaignflow.identity.dto.request.UpdateUserRequest;
import me.mallahajay43.campaignflow.identity.dto.request.UserCreateRequest;
import me.mallahajay43.campaignflow.identity.dto.response.UserResponse;
import me.mallahajay43.campaignflow.identity.entity.Tenant;
import me.mallahajay43.campaignflow.identity.entity.User;
import me.mallahajay43.campaignflow.identity.mapper.UserMapper;
import me.mallahajay43.campaignflow.identity.repository.TenantRepository;
import me.mallahajay43.campaignflow.identity.repository.UserRepository;
import me.mallahajay43.campaignflow.identity.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TenantRepository tenantRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<UserResponse> fetchUsers(UUID tenantId) {
        return userMapper.toResponseList(userRepository.findByTenant_Id(tenantId));
    }

    @Override
    public UserResponse fetchUser(UUID userId, UUID tenantId) {
        return userMapper.toResponse(userRepository.findByIdAndTenant_Id(userId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("USER", userId)));
    }

    @Override
    @Transactional
    public UserResponse createUser(UUID tenantId, UserCreateRequest request) {

        Optional<User> user = userRepository.findByEmail(request.email());
        if (user.isPresent()) {
            throw new DuplicateResourceException("USER", "User already exists with email: " + request.email());
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("TENANT", tenantId));

        User newUser = userMapper.toEntityFromUserCreateRequest(request);
        newUser.setTenant(tenant);
        newUser.setStatus(UserStatus.ACTIVE);
        newUser.setPasswordHash(bCryptPasswordEncoder.encode(request.password()));
        newUser = userRepository.save(newUser);

        return userMapper.toResponse(newUser);
    }

    @Override
    public UserResponse changeRole(UUID userId, ChangeUserRoleRequest request, UUID tenantId) {
        User user = userRepository.findByIdAndTenant_Id(userId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("USER", userId));

        user.setRole(request.role());
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse suspend(UUID userId, UUID tenantId) {
        User user = userRepository.findByIdAndTenant_Id(userId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("USER", userId));

        user.setStatus(UserStatus.BLOCKED);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse activate(UUID userId, UUID tenantId) {
        User user = userRepository.findByIdAndTenant_Id(userId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("USER", userId));

        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public void delete(UUID userId, UUID tenantId) {
        User user = userRepository.findByIdAndTenant_Id(userId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("USER", userId));
        userRepository.delete(user);
    }

    @Override
    public UserResponse fetchCurrentUser(UUID userId, UUID tenantId) {
        return userMapper.toResponse(userRepository.findByIdAndTenant_Id(userId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("USER", userId)));
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID userId, UpdateUserRequest request, UUID tenantId) {

        User user = userRepository.findByIdAndTenant_Id(userId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("USER", userId));

        if (request.email() != null && !user.getEmail().equals(request.email())) {
            Optional<User> userOptional = userRepository.findByEmail(request.email());
            if (userOptional.isPresent()) {
                throw new DuplicateResourceException("USER", "User already exists with email: " + request.email());
            }
        }
        userMapper.updateEntityFromRecord(request, user);
        user.setPasswordHash(bCryptPasswordEncoder.encode(request.password()));
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }
}
