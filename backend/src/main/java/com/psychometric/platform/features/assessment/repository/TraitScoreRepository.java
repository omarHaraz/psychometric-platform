package com.psychometric.platform.features.assessment.repository;

import com.psychometric.platform.features.assessment.domain.model.TraitScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraitScoreRepository extends JpaRepository<TraitScore, Long> {
    List<TraitScore> findByAssessmentScoreId(Long assessmentScoreId);
}
