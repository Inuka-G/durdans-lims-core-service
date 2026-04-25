package com.uom.lims.api.supermanagement.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditLogItemResponse {
    private String id;
    private String action;
    private String entityType;
    private String entityId;
    private String patientCode;
    private String performedBy;
    private String branchCode;
    private String ipAddress;
    private String timestamp;
    private String details;
}

