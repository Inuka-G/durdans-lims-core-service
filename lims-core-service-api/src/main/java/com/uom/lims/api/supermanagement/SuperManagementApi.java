package com.uom.lims.api.supermanagement;

import com.uom.lims.api.common.PageResponse;
import com.uom.lims.api.dto.response.ApiResponse;
import com.uom.lims.api.supermanagement.dto.request.GlobalUserCreateRequest;
import com.uom.lims.api.supermanagement.dto.request.GlobalUserUpdateRequest;
import com.uom.lims.api.supermanagement.dto.request.SuperBranchCreateRequest;
import com.uom.lims.api.supermanagement.dto.request.SuperBranchUpdateRequest;
import com.uom.lims.api.supermanagement.dto.response.GlobalUserResponse;
import com.uom.lims.api.supermanagement.dto.response.SuperBranchResponse;
import com.uom.lims.api.supermanagement.dto.response.SuperReportsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/super-management")
@Tag(name = "Super Management", description = "Super-admin only APIs for system-wide branch and user administration")
public interface SuperManagementApi {

    @Operation(summary = "Get all branches")
    @GetMapping("/branches")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<PageResponse<SuperBranchResponse>>> getAllBranches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search);

    @Operation(summary = "Create branch")
    @PostMapping("/branches")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ApiResponse<SuperBranchResponse>> createBranch(@RequestBody SuperBranchCreateRequest request);

    @Operation(summary = "Update branch")
    @PutMapping("/branches/{branchId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<SuperBranchResponse>> updateBranch(
            @PathVariable UUID branchId,
            @RequestBody SuperBranchUpdateRequest request);

    @Operation(summary = "Get all audit logs (all branches)")
    @GetMapping("/audit-logs")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<PageResponse<com.uom.lims.api.supermanagement.dto.response.AuditLogItemResponse>>> getAllBranchAuditLogs(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    @Operation(summary = "Get global reports (all branches)")
    @GetMapping("/reports")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<SuperReportsResponse>> getAllBranchReports();

    @Operation(summary = "Create global user")
    @PostMapping("/global-users")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ApiResponse<GlobalUserResponse>> createGlobalUser(@RequestBody GlobalUserCreateRequest request);

    @Operation(summary = "Update global user")
    @PutMapping("/global-users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<GlobalUserResponse>> updateGlobalUser(
            @PathVariable UUID userId,
            @RequestBody GlobalUserUpdateRequest request);

    @Operation(summary = "Get global users")
    @GetMapping("/global-users")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<PageResponse<GlobalUserResponse>>> getGlobalUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search);
}

