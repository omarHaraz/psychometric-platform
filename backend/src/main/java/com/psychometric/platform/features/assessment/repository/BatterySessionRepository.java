package com.psychometric.platform.features.assessment.repository;

import com.psychometric.platform.features.assessment.domain.enums.SessionState;
import com.psychometric.platform.features.assessment.domain.model.BatterySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatterySessionRepository extends JpaRepository<BatterySession, Long> {
    List<BatterySession> findByState(SessionState state);
}
