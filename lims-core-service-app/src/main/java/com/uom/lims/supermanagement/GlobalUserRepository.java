package com.uom.lims.supermanagement;

import com.uom.lims.entity.GlobalUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GlobalUserRepository extends JpaRepository<GlobalUserEntity, UUID> {

    boolean existsByEmailAddress(String emailAddress);

    @Query("""
            SELECT u FROM GlobalUserEntity u
            WHERE (:search IS NULL OR
                lower(u.fullName) LIKE lower(concat('%', :search, '%')) OR
                lower(u.emailAddress) LIKE lower(concat('%', :search, '%')) OR
                lower(u.branchCode) LIKE lower(concat('%', :search, '%'))
            )
            """)
    Page<GlobalUserEntity> search(@Param("search") String search, Pageable pageable);
}

