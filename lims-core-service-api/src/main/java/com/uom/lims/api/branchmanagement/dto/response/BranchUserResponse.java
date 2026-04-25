package com.uom.lims.api.branchmanagement.dto.response;

import com.uom.lims.api.branchmanagement.dto.enums.BranchAccountStatus;
import com.uom.lims.api.branchmanagement.dto.enums.BranchRoleAssignment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchUserResponse {
    private UUID id;
    private String branchCode;
    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private String username;
    private BranchAccountStatus accountStatus;
    private BranchRoleAssignment roleAssignment;
    private Instant createdAt;
    private Instant updatedAt;
}

