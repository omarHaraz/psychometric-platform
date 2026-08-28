package com.psychometric.platform.features.assessment.repository;

import com.psychometric.platform.features.assessment.domain.model.CandidateResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateResponseRepository extends JpaRepository<CandidateResponse, Long> {
}
