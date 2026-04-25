package com.uom.lims.api.supermanagement.dto.response;

import com.uom.lims.api.supermanagement.dto.enums.BranchOperationalStatus;
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
public class SuperBranchResponse {
    private UUID id;
    private String code;
    private String name;
    private String location;
    private String contactEmail;
    private String contactPhone;
    private BranchOperationalStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}

