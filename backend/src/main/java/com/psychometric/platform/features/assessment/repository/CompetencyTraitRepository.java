package com.psychometric.platform.features.assessment.repository;

import com.psychometric.platform.features.assessment.domain.model.CompetencyTrait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetencyTraitRepository extends JpaRepository<CompetencyTrait, Long> {
    Optional<CompetencyTrait> findByCode(String code);
    List<CompetencyTrait> findAllByOrderByDisplayOrderAsc();
}
