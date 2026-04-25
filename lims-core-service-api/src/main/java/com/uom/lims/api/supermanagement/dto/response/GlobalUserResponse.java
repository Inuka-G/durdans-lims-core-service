package com.uom.lims.api.supermanagement.dto.response;

import com.uom.lims.api.supermanagement.dto.enums.GlobalAccountStatus;
import com.uom.lims.api.supermanagement.dto.enums.GlobalUserRole;
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
public class GlobalUserResponse {
    private UUID id;
    private String fullName;
    private String emailAddress;
    private String branchCode;
    private GlobalUserRole role;
    private GlobalAccountStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}

