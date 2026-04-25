package com.uom.lims.api.supermanagement.dto.request;

import com.uom.lims.api.supermanagement.dto.enums.BranchOperationalStatus;
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
public class SuperBranchCreateRequest {
    private String branchName;
    private String location;
    private String contactEmail;
    private String contactPhone;
    private BranchOperationalStatus initialStatus;
}

