package com.uom.lims.entity;

import com.uom.lims.api.supermanagement.dto.enums.GlobalAccountStatus;
import com.uom.lims.api.supermanagement.dto.enums.GlobalUserRole;
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
@Table(name = "global_user")
public class GlobalUserEntity extends BaseEntity {

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @Column(name = "branch_code", nullable = false)
    private String branchCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private GlobalUserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GlobalAccountStatus status;
}

