package com.psychometric.platform.features.assessment.repository;

import com.psychometric.platform.features.assessment.domain.model.DerailerCategoryScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DerailerCategoryScoreRepository extends JpaRepository<DerailerCategoryScore, Long> {
    List<DerailerCategoryScore> findByAssessmentScoreId(Long assessmentScoreId);
}
