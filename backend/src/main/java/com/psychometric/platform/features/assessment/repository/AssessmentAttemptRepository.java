package com.psychometric.platform.features.assessment.repository;

import com.psychometric.platform.features.assessment.domain.enums.AttemptState;
import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentAttemptRepository extends JpaRepository<AssessmentAttempt, Long> {
    Optional<AssessmentAttempt> findByAttemptToken(String attemptToken);
    List<AssessmentAttempt> findByCandidateId(Long candidateId);
    boolean existsByCandidateIdAndStateNot(Long candidateId, AttemptState state);
}
