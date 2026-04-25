package com.uom.lims.entity;

import com.uom.lims.api.branchmanagement.dto.enums.BranchAccountStatus;
import com.uom.lims.api.branchmanagement.dto.enums.BranchRoleAssignment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "branch_user")
public class BranchUserEntity extends BaseEntity {

    @Column(name = "branch_code", nullable = false)
    private String branchCode;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "username", nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private BranchAccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_assignment", nullable = false)
    private BranchRoleAssignment roleAssignment;
}

