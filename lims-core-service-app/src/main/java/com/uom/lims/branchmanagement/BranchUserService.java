package com.uom.lims.branchmanagement;

import com.uom.lims.api.branchmanagement.dto.request.BranchUserCreateRequest;
import com.uom.lims.api.branchmanagement.dto.request.BranchUserUpdateRequest;
import com.uom.lims.api.branchmanagement.dto.enums.BranchAccountStatus;
import com.uom.lims.api.branchmanagement.dto.enums.BranchRoleAssignment;
import com.uom.lims.api.branchmanagement.dto.response.BranchUserResponse;
import com.uom.lims.entity.BranchUserEntity;
import com.uom.lims.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BranchUserService {

    private final BranchUserRepository branchUserRepository;

    public Page<BranchUserResponse> getBranchUsers(Pageable pageable, String search) {
        String branchCode = requireBranchCode();
        String normalizedSearch = StringUtils.hasText(search) ? search.trim() : null;

        Page<BranchUserEntity> page = normalizedSearch == null
                ? branchUserRepository.findAllByBranchCode(branchCode, pageable)
                : branchUserRepository.searchByBranchCode(branchCode, normalizedSearch, pageable);

        return page.map(this::toResponse);
    }

    public BranchUserResponse createBranchUser(BranchUserCreateRequest request) {
        String branchCode = requireBranchCode();

        BranchUserEntity entity = new BranchUserEntity();
        entity.setBranchCode(branchCode);
        apply(entity, request.getFullName(), request.getEmailAddress(), request.getPhoneNumber(), request.getUsername(),
                request.getAccountStatus(), request.getRoleAssignment());

        if (branchUserRepository.existsByUsername(entity.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        BranchUserEntity saved = branchUserRepository.save(entity);
        return toResponse(saved);
    }

    public BranchUserResponse updateBranchUser(UUID userId, BranchUserUpdateRequest request) {
        String branchCode = requireBranchCode();

        BranchUserEntity entity = branchUserRepository.findByIdAndBranchCode(userId, branchCode)
                .orElseThrow(() -> new IllegalArgumentException("Branch user not found"));

        String newUsername = StringUtils.hasText(request.getUsername()) ? request.getUsername().trim() : entity.getUsername();
        if (!newUsername.equals(entity.getUsername()) && branchUserRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("Username already exists");
        }

        apply(entity,
                request.getFullName(),
                request.getEmailAddress(),
                request.getPhoneNumber(),
                newUsername,
                request.getAccountStatus(),
                request.getRoleAssignment());

        BranchUserEntity saved = branchUserRepository.save(entity);
        return toResponse(saved);
    }

    private void apply(
            BranchUserEntity entity,
            String fullName,
            String emailAddress,
            String phoneNumber,
            String username,
            BranchAccountStatus accountStatus,
            BranchRoleAssignment roleAssignment) {

        if (!StringUtils.hasText(fullName)) throw new IllegalArgumentException("Full name is required");
        if (!StringUtils.hasText(emailAddress)) throw new IllegalArgumentException("Email address is required");
        if (!StringUtils.hasText(username)) throw new IllegalArgumentException("Username is required");
        if (accountStatus == null) throw new IllegalArgumentException("Account status is required");
        if (roleAssignment == null) throw new IllegalArgumentException("Role assignment is required");

        entity.setFullName(fullName.trim());
        entity.setEmailAddress(emailAddress.trim());
        entity.setPhoneNumber(StringUtils.hasText(phoneNumber) ? phoneNumber.trim() : null);
        entity.setUsername(username.trim());
        entity.setAccountStatus(accountStatus);
        entity.setRoleAssignment(roleAssignment);
    }

    private BranchUserResponse toResponse(BranchUserEntity e) {
        return BranchUserResponse.builder()
                .id(e.getId())
                .branchCode(e.getBranchCode())
                .fullName(e.getFullName())
                .emailAddress(e.getEmailAddress())
                .phoneNumber(e.getPhoneNumber())
                .username(e.getUsername())
                .accountStatus(e.getAccountStatus())
                .roleAssignment(e.getRoleAssignment())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getLastModifiedAt())
                .build();
    }

    private String requireBranchCode() {
        String branchCode = SecurityUtils.getCurrentBranchId();
        if (!StringUtils.hasText(branchCode)) {
            throw new IllegalStateException("Branch code missing in token");
        }
        return branchCode;
    }
}

