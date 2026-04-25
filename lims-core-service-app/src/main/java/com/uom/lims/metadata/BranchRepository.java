package com.uom.lims.metadata;

import com.uom.lims.entity.BranchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, UUID> {
    Optional<BranchEntity> findByCode(String code);

    @Query("""
            SELECT b FROM BranchEntity b
            WHERE (:search IS NULL OR
                lower(b.name) LIKE lower(concat('%', :search, '%')) OR
                lower(b.code) LIKE lower(concat('%', :search, '%')) OR
                lower(coalesce(b.location, '')) LIKE lower(concat('%', :search, '%'))
            )
            """)
    Page<BranchEntity> search(@Param("search") String search, Pageable pageable);
}
