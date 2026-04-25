package com.uom.lims.api.branchmanagement.dto.request;

import com.uom.lims.api.branchmanagement.dto.enums.BranchAccountStatus;
import com.uom.lims.api.branchmanagement.dto.enums.BranchRoleAssignment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchUserUpdateRequest {
    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private String username;
    private BranchAccountStatus accountStatus;
    private BranchRoleAssignment roleAssignment;
}

