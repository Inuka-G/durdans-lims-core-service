package com.uom.lims.api.branchmanagement;

import com.uom.lims.api.branchmanagement.dto.request.BranchUserCreateRequest;
import com.uom.lims.api.branchmanagement.dto.request.BranchUserUpdateRequest;
import com.uom.lims.api.branchmanagement.dto.response.BranchReportsResponse;
import com.uom.lims.api.branchmanagement.dto.response.BranchUserResponse;
import com.uom.lims.api.common.PageResponse;
import com.uom.lims.api.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/branch-management")
@Tag(name = "Branch Management", description = "Branch-admin only APIs for managing branch users, logs, and reports")
public interface BranchManagementApi {

    @Operation(summary = "Get branch users", description = "Lists users assigned to the current branch")
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<PageResponse<BranchUserResponse>>> getBranchUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search);

    @Operation(summary = "Create branch user", description = "Creates a new user assigned to the current branch")
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ApiResponse<BranchUserResponse>> createBranchUser(@RequestBody BranchUserCreateRequest request);

    @Operation(summary = "Update branch user", description = "Updates an existing branch user in the current branch")
    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<BranchUserResponse>> updateBranchUser(
            @PathVariable UUID userId,
            @RequestBody BranchUserUpdateRequest request);

    @Operation(summary = "Get branch reports", description = "Returns branch-level dashboard reports for the current branch")
    @GetMapping("/reports")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ApiResponse<BranchReportsResponse>> getBranchReports();
}

