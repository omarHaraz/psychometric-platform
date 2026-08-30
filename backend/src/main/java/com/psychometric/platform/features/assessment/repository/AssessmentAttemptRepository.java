package com.psychometric.platform.features.assessment.repository;

import com.psychometric.platform.features.assessment.domain.enums.AttemptState;
import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentAttemptRepository extends JpaRepository<AssessmentAttempt, Long> {
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"candidate", "createdBy", "batterySessions"})
    Optional<AssessmentAttempt> findByAttemptToken(String attemptToken);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"candidate", "createdBy", "batterySessions"})
    List<AssessmentAttempt> findByCandidateIdOrderByCreatedAtDesc(Long candidateId);

    boolean existsByCandidateIdAndStateNot(Long candidateId, AttemptState state);
    boolean existsByCandidateIdAndStateIn(Long candidateId, List<AttemptState> states);
}
