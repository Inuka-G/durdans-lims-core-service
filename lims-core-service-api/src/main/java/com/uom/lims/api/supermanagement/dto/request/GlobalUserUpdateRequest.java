package com.uom.lims.api.supermanagement.dto.request;

import com.uom.lims.api.supermanagement.dto.enums.GlobalAccountStatus;
import com.uom.lims.api.supermanagement.dto.enums.GlobalUserRole;
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
public class GlobalUserUpdateRequest {
    private String fullName;
    private String emailAddress;
    private String branchCode;
    private GlobalUserRole role;
    private GlobalAccountStatus status;
}

