package com.uom.lims.branchmanagement;

import com.uom.lims.api.branchmanagement.BranchManagementApi;
import com.uom.lims.api.branchmanagement.dto.response.BranchReportsResponse;
import com.uom.lims.api.branchmanagement.dto.request.BranchUserCreateRequest;
import com.uom.lims.api.branchmanagement.dto.request.BranchUserUpdateRequest;
import com.uom.lims.api.branchmanagement.dto.response.BranchUserResponse;
import com.uom.lims.api.common.PageResponse;
import com.uom.lims.api.dto.response.ApiResponse;
import com.uom.lims.audit.AuditLog;
import com.uom.lims.audit.AuditLogRepository;
import com.uom.lims.audit.AuditLogResponse;
import com.uom.lims.security.SecurityUtils;
import com.uom.lims.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('BRANCH_ADMIN')")
public class BranchManagementController implements BranchManagementApi {

    private final BranchUserService branchUserService;
    private final AuditLogRepository auditLogRepository;
    private final StatisticsService statisticsService;

    @Override
    public ResponseEntity<ApiResponse<PageResponse<BranchUserResponse>>> getBranchUsers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BranchUserResponse> users = branchUserService.getBranchUsers(pageable, search);

        PageResponse<BranchUserResponse> payload = new PageResponse<>(
                users.getContent(),
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isLast());

        return ResponseEntity.ok(ApiResponse.success(payload));
    }

    @Override
    public ResponseEntity<ApiResponse<BranchUserResponse>> createBranchUser(BranchUserCreateRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.success(branchUserService.createBranchUser(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<BranchUserResponse>> updateBranchUser(UUID userId, BranchUserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(branchUserService.updateBranchUser(userId, request)));
    }

    @Override
    public ResponseEntity<ApiResponse<BranchReportsResponse>> getBranchReports() {
        BranchReportsResponse response = BranchReportsResponse.builder()
                .ordersBilling(statisticsService.getOrdersBillingStats())
                .phlebotomy(statisticsService.getPhlebotomyStats())
                .build();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/api/v1/branch-management/audit-logs")
    public ResponseEntity<ApiResponse<PageResponse<AuditLogResponse>>> getBranchAuditLogs(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        String branchCode = SecurityUtils.getCurrentBranchId();
        if (branchCode == null || branchCode.isBlank()) {
            throw new IllegalStateException("Branch code missing in token");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<AuditLog> result = auditLogRepository.findByBranchCodeFiltered(
                branchCode,
                normalize(action),
                normalize(entityType),
                normalize(performedBy),
                normalize(search),
                pageable);

        List<AuditLogResponse> responses = result.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        PageResponse<AuditLogResponse> payload = new PageResponse<>(
                responses,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast());

        return ResponseEntity.ok(ApiResponse.success(payload));
    }

    private String normalize(String s) {
        return (s != null && !s.isBlank()) ? s.trim() : null;
    }

    private AuditLogResponse toResponse(AuditLog auditLog) {
        AuditLogResponse r = new AuditLogResponse();
        r.setId(auditLog.getId() != null ? auditLog.getId().toString() : "");
        r.setAction(auditLog.getAction());
        r.setEntityType(auditLog.getEntityType());
        r.setEntityId(auditLog.getEntityId() != null ? auditLog.getEntityId().toString() : null);
        r.setPatientCode(auditLog.getPatientCode());
        r.setPerformedBy(auditLog.getPerformedBy());
        r.setBranchCode(auditLog.getBranchCode());
        r.setIpAddress(auditLog.getIpAddress());
        r.setTimestamp(auditLog.getTimestamp() != null ? auditLog.getTimestamp().toString() : "");
        r.setDetails(auditLog.getDetails());
        return r;
    }
}

