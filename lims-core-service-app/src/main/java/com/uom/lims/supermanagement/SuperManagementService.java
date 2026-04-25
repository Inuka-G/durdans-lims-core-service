package com.uom.lims.supermanagement;

import com.uom.lims.api.supermanagement.dto.enums.GlobalAccountStatus;
import com.uom.lims.api.supermanagement.dto.request.GlobalUserCreateRequest;
import com.uom.lims.api.supermanagement.dto.request.GlobalUserUpdateRequest;
import com.uom.lims.api.supermanagement.dto.request.SuperBranchCreateRequest;
import com.uom.lims.api.supermanagement.dto.request.SuperBranchUpdateRequest;
import com.uom.lims.api.supermanagement.dto.response.GlobalUserResponse;
import com.uom.lims.api.supermanagement.dto.response.SuperBranchResponse;
import com.uom.lims.entity.BranchEntity;
import com.uom.lims.entity.GlobalUserEntity;
import com.uom.lims.metadata.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SuperManagementService {

    private final BranchRepository branchRepository;
    private final GlobalUserRepository globalUserRepository;

    @Transactional(readOnly = true)
    public Page<SuperBranchResponse> getAllBranches(Pageable pageable, String search) {
        String normalized = StringUtils.hasText(search) ? search.trim() : null;
        return branchRepository.search(normalized, pageable).map(this::toResponse);
    }

    public SuperBranchResponse createBranch(SuperBranchCreateRequest request) {
        if (!StringUtils.hasText(request.getBranchName())) throw new IllegalArgumentException("Branch name is required");
        if (request.getInitialStatus() == null) throw new IllegalArgumentException("Initial status is required");

        BranchEntity entity = new BranchEntity();
        entity.setName(request.getBranchName().trim());
        entity.setLocation(StringUtils.hasText(request.getLocation()) ? request.getLocation().trim() : null);
        entity.setContactEmail(StringUtils.hasText(request.getContactEmail()) ? request.getContactEmail().trim() : null);
        entity.setContactPhone(StringUtils.hasText(request.getContactPhone()) ? request.getContactPhone().trim() : null);
        entity.setStatus(request.getInitialStatus());
        entity.setCode(generateBranchCode(entity.getName()));

        return toResponse(branchRepository.save(entity));
    }

    public SuperBranchResponse updateBranch(UUID branchId, SuperBranchUpdateRequest request) {
        BranchEntity entity = branchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found"));

        if (StringUtils.hasText(request.getBranchName())) {
            entity.setName(request.getBranchName().trim());
        }
        if (request.getLocation() != null) {
            entity.setLocation(StringUtils.hasText(request.getLocation()) ? request.getLocation().trim() : null);
        }
        if (request.getContactEmail() != null) {
            entity.setContactEmail(StringUtils.hasText(request.getContactEmail()) ? request.getContactEmail().trim() : null);
        }
        if (request.getContactPhone() != null) {
            entity.setContactPhone(StringUtils.hasText(request.getContactPhone()) ? request.getContactPhone().trim() : null);
        }
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        return toResponse(branchRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public Page<GlobalUserResponse> getGlobalUsers(Pageable pageable, String search) {
        String normalized = StringUtils.hasText(search) ? search.trim() : null;
        return globalUserRepository.search(normalized, pageable).map(this::toResponse);
    }

    public GlobalUserResponse createGlobalUser(GlobalUserCreateRequest request) {
        if (!StringUtils.hasText(request.getFullName())) throw new IllegalArgumentException("Full name is required");
        if (!StringUtils.hasText(request.getEmailAddress())) throw new IllegalArgumentException("Email address is required");
        if (!StringUtils.hasText(request.getBranchCode())) throw new IllegalArgumentException("Branch code is required");
        if (request.getRole() == null) throw new IllegalArgumentException("Role is required");
        if (request.getInitialStatus() == null) throw new IllegalArgumentException("Initial status is required");

        String email = request.getEmailAddress().trim().toLowerCase(Locale.ROOT);
        if (globalUserRepository.existsByEmailAddress(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        GlobalUserEntity entity = new GlobalUserEntity();
        entity.setFullName(request.getFullName().trim());
        entity.setEmailAddress(email);
        entity.setBranchCode(request.getBranchCode().trim().toUpperCase(Locale.ROOT));
        entity.setRole(request.getRole());
        entity.setStatus(request.getInitialStatus());

        return toResponse(globalUserRepository.save(entity));
    }

    public GlobalUserResponse updateGlobalUser(UUID userId, GlobalUserUpdateRequest request) {
        GlobalUserEntity entity = globalUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Global user not found"));

        if (StringUtils.hasText(request.getFullName())) {
            entity.setFullName(request.getFullName().trim());
        }
        if (StringUtils.hasText(request.getEmailAddress())) {
            String email = request.getEmailAddress().trim().toLowerCase(Locale.ROOT);
            if (!email.equals(entity.getEmailAddress()) && globalUserRepository.existsByEmailAddress(email)) {
                throw new IllegalArgumentException("Email already exists");
            }
            entity.setEmailAddress(email);
        }
        if (StringUtils.hasText(request.getBranchCode())) {
            entity.setBranchCode(request.getBranchCode().trim().toUpperCase(Locale.ROOT));
        }
        if (request.getRole() != null) {
            entity.setRole(request.getRole());
        }
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        return toResponse(globalUserRepository.save(entity));
    }

    private SuperBranchResponse toResponse(BranchEntity e) {
        return SuperBranchResponse.builder()
                .id(e.getId())
                .code(e.getCode())
                .name(e.getName())
                .location(e.getLocation())
                .contactEmail(e.getContactEmail())
                .contactPhone(e.getContactPhone())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getLastModifiedAt())
                .build();
    }

    private GlobalUserResponse toResponse(GlobalUserEntity e) {
        return GlobalUserResponse.builder()
                .id(e.getId())
                .fullName(e.getFullName())
                .emailAddress(e.getEmailAddress())
                .branchCode(e.getBranchCode())
                .role(e.getRole())
                .status(e.getStatus() != null ? e.getStatus() : GlobalAccountStatus.ACTIVE)
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getLastModifiedAt())
                .build();
    }

    private String generateBranchCode(String branchName) {
        String base = branchName.trim().toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]+", "-");
        base = base.length() > 8 ? base.substring(0, 8) : base;
        if (base.isBlank()) base = "BRANCH";
        return base + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase(Locale.ROOT);
    }
}

