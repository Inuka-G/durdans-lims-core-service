package com.uom.lims.supermanagement;

import com.uom.lims.api.common.PageResponse;
import com.uom.lims.api.dto.response.ApiResponse;
import com.uom.lims.api.supermanagement.SuperManagementApi;
import com.uom.lims.api.supermanagement.dto.response.AuditLogItemResponse;
import com.uom.lims.api.supermanagement.dto.response.GlobalUserResponse;
import com.uom.lims.api.supermanagement.dto.response.SuperBranchResponse;
import com.uom.lims.api.supermanagement.dto.response.SuperReportsResponse;
import com.uom.lims.api.supermanagement.dto.request.GlobalUserCreateRequest;
import com.uom.lims.api.supermanagement.dto.request.GlobalUserUpdateRequest;
import com.uom.lims.api.supermanagement.dto.request.SuperBranchCreateRequest;
import com.uom.lims.api.supermanagement.dto.request.SuperBranchUpdateRequest;
import com.uom.lims.audit.AuditLog;
import com.uom.lims.audit.AuditLogRepository;
import com.uom.lims.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperManagementController implements SuperManagementApi {

    private final SuperManagementService superManagementService;
    private final AuditLogRepository auditLogRepository;
    private final StatisticsService statisticsService;

    @Override
    public ResponseEntity<ApiResponse<PageResponse<SuperBranchResponse>>> getAllBranches(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<SuperBranchResponse> branches = superManagementService.getAllBranches(pageable, search);
        return ResponseEntity.ok(ApiResponse.success(toPageResponse(branches)));
    }

    @Override
    public ResponseEntity<ApiResponse<SuperBranchResponse>> createBranch(SuperBranchCreateRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.success(superManagementService.createBranch(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<SuperBranchResponse>> updateBranch(UUID branchId, SuperBranchUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(superManagementService.updateBranch(branchId, request)));
    }

    @Override
    public ResponseEntity<ApiResponse<PageResponse<AuditLogItemResponse>>> getAllBranchAuditLogs(
            String action,
            String entityType,
            String performedBy,
            String search,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<AuditLog> result = auditLogRepository.findAllFiltered(
                normalize(action),
                normalize(entityType),
                normalize(performedBy),
                normalize(search),
                pageable);

        List<AuditLogItemResponse> items = result.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        PageResponse<AuditLogItemResponse> payload = new PageResponse<>(
                items,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast());

        return ResponseEntity.ok(ApiResponse.success(payload));
    }

    @Override
    public ResponseEntity<ApiResponse<SuperReportsResponse>> getAllBranchReports() {
        SuperReportsResponse response = SuperReportsResponse.builder()
                .ordersBilling(statisticsService.getOrdersBillingStats())
                .phlebotomy(statisticsService.getPhlebotomyStats())
                .build();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Override
    public ResponseEntity<ApiResponse<GlobalUserResponse>> createGlobalUser(GlobalUserCreateRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.success(superManagementService.createGlobalUser(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<GlobalUserResponse>> updateGlobalUser(UUID userId, GlobalUserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(superManagementService.updateGlobalUser(userId, request)));
    }

    @Override
    public ResponseEntity<ApiResponse<PageResponse<GlobalUserResponse>>> getGlobalUsers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<GlobalUserResponse> users = superManagementService.getGlobalUsers(pageable, search);
        return ResponseEntity.ok(ApiResponse.success(toPageResponse(users)));
    }

    private <T> PageResponse<T> toPageResponse(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }

    private String normalize(String s) {
        return (s != null && !s.isBlank()) ? s.trim() : null;
    }

    private AuditLogItemResponse toResponse(AuditLog auditLog) {
        AuditLogItemResponse r = new AuditLogItemResponse();
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

