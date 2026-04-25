package com.uom.lims.branchmanagement;

import com.uom.lims.entity.BranchUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchUserRepository extends JpaRepository<BranchUserEntity, UUID> {

    Page<BranchUserEntity> findAllByBranchCode(String branchCode, Pageable pageable);

    @Query("""
            SELECT u FROM BranchUserEntity u
            WHERE u.branchCode = :branchCode
              AND (
                :search IS NULL OR
                lower(u.fullName) LIKE lower(concat('%', :search, '%')) OR
                lower(u.emailAddress) LIKE lower(concat('%', :search, '%')) OR
                lower(u.username) LIKE lower(concat('%', :search, '%'))
              )
            """)
    Page<BranchUserEntity> searchByBranchCode(
            @Param("branchCode") String branchCode,
            @Param("search") String search,
            Pageable pageable);

    Optional<BranchUserEntity> findByIdAndBranchCode(UUID id, String branchCode);

    boolean existsByUsername(String username);
}

