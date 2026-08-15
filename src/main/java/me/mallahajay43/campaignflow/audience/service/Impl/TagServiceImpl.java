package me.mallahajay43.campaignflow.audience.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.audience.dto.request.CreateTagRequest;
import me.mallahajay43.campaignflow.audience.dto.response.TagResponse;
import me.mallahajay43.campaignflow.audience.entity.ContactTag;
import me.mallahajay43.campaignflow.audience.entity.Tag;
import me.mallahajay43.campaignflow.audience.mapper.TagMapper;
import me.mallahajay43.campaignflow.audience.repository.ContactRepository;
import me.mallahajay43.campaignflow.audience.repository.ContactTagRepository;
import me.mallahajay43.campaignflow.audience.repository.TagRepository;
import me.mallahajay43.campaignflow.audience.service.TagService;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.common.exceptions.DuplicateResourceException;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final TenantContext tenantContext;
    private final ContactRepository contactRepository;
    private final ContactTagRepository contactTagRepository;

    @Override
    @Transactional
    public TagResponse create(CreateTagRequest request) {
        UUID tenantId = tenantContext.getTenantId();

        if (tagRepository.existsByNameIgnoreCaseAndTenantId(request.name(), tenantId)) {
            throw new DuplicateResourceException("TAG", "Tag Already Exists, name: " + request.name());
        }

        Tag tag = tagMapper.toEntityFromCreateTagRequest(request);
        tag.setTenantId(tenantId);
        tag = tagRepository.save(tag);
        return tagMapper.toResponse(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> findAll() {
        UUID tenantId = tenantContext.getTenantId();
        return tagMapper.toResponseList(tagRepository.findAllByTenantId(tenantId));
    }

    @Override
    @Transactional
    public void delete(UUID tagId) {
        UUID tenantId = tenantContext.getTenantId();
        Tag tag = tagRepository.findByIdAndTenantId(tagId, tenantId).orElseThrow(
                () -> new ResourceNotFoundException("TAG", tagId));
        tagRepository.delete(tag);
    }

    @Override
    @Transactional
    public void assignTag(UUID contactId, UUID tagId) {
        UUID tenantId = tenantContext.getTenantId();
        validateTag(tagId, tenantId);
        validateContact(contactId, tenantId);

        if (contactTagRepository
                .existsByTenantIdAndContactIdAndTagId(tenantId, contactId, tagId)
        ) {
            return;
        }

        ContactTag contactTag = ContactTag.builder()
                .tenantId(tenantId)
                .contactId(contactId)
                .tagId(tagId)
                .build();

        contactTagRepository.save(contactTag);
    }

    @Override
    @Transactional
    public void removeTag(UUID contactId, UUID tagId) {
        UUID tenantId = tenantContext.getTenantId();

        contactTagRepository
                .deleteByTenantIdAndContactIdAndTagId(tenantId, contactId, tagId);
    }

    private void validateTag(UUID tagId, UUID tenantId) {
        tagRepository.findByIdAndTenantId(tagId, tenantId).orElseThrow(
                () -> new ResourceNotFoundException("TAG", tagId));
    }

    private void validateContact(UUID contactId, UUID tenantId) {
        contactRepository.findByIdAndTenantId(contactId, tenantId).orElseThrow(
                () -> new ResourceNotFoundException("CONTACT", contactId)
        );
    }
}
