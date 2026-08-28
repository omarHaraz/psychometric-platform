package com.psychometric.platform.features.assessment.service;

import com.psychometric.platform.features.assessment.domain.enums.SessionState;
import com.psychometric.platform.features.assessment.domain.model.BatterySession;
import com.psychometric.platform.features.assessment.repository.BatterySessionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionTimeoutJob {

    private final BatterySessionRepository sessionRepo;
    private final AssessmentSessionService sessionService;

    public SessionTimeoutJob(BatterySessionRepository sessionRepo, AssessmentSessionService sessionService) {
        this.sessionRepo = sessionRepo;
        this.sessionService = sessionService;
    }

    @Scheduled(fixedDelay = 15000) // run every 15 seconds
    public void sweepTimedOutSessions() {
        List<BatterySession> inProgress = sessionRepo.findByState(SessionState.IN_PROGRESS);
        for (BatterySession session : inProgress) {
            try {
                // If it takes longer than the limit, auto-submit
                long elapsed = System.currentTimeMillis() - session.getStartTime().toEpochMilli();
                if (elapsed > (session.getTimeLimitSeconds() * 1000L)) {
                    sessionService.autoSubmitSession(session);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
