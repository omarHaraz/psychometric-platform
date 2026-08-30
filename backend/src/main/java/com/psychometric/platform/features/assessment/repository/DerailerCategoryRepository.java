package com.psychometric.platform.features.assessment.repository;

import com.psychometric.platform.features.assessment.domain.model.DerailerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DerailerCategoryRepository extends JpaRepository<DerailerCategory, Long> {
    Optional<DerailerCategory> findByNameAr(String nameAr);
    List<DerailerCategory> findAllByOrderByDisplayOrderAsc();
}
