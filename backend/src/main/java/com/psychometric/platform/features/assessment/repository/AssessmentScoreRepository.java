package com.psychometric.platform.features.assessment.repository;

import com.psychometric.platform.features.assessment.domain.model.AssessmentScore;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssessmentScoreRepository extends JpaRepository<AssessmentScore, Long> {

    @EntityGraph(attributePaths = {"attempt", "attempt.candidate", "traitScores", "traitScores.trait", "derailerCategoryScores", "derailerCategoryScores.category", "gcatSubtestScores"})
    Optional<AssessmentScore> findByAttemptId(Long attemptId);

    @EntityGraph(attributePaths = {"attempt", "attempt.candidate", "traitScores", "traitScores.trait", "derailerCategoryScores", "derailerCategoryScores.category", "gcatSubtestScores"})
    Optional<AssessmentScore> findByAttemptAttemptToken(String attemptToken);
}
