package com.psychometric.platform.features.report;

import com.psychometric.platform.features.assessment.domain.enums.AttemptState;
import com.psychometric.platform.features.assessment.domain.enums.BatteryType;
import com.psychometric.platform.features.assessment.domain.enums.SessionState;
import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import com.psychometric.platform.features.assessment.domain.model.AssessmentScore;
import com.psychometric.platform.features.assessment.domain.model.BatterySession;
import com.psychometric.platform.features.assessment.repository.AssessmentAttemptRepository;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto;
import com.psychometric.platform.features.assessment.service.AssessmentScoringService;
import com.psychometric.platform.features.report.service.LeadershipReportGeneratorService;
import com.psychometric.platform.features.user.entity.User;
import com.psychometric.platform.features.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
public class EmptyTestScoringAndReportRejectionTest {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AssessmentAttemptRepository attemptRepo;

    @Autowired
    private AssessmentScoringService scoringService;

    @Autowired
    private LeadershipReportGeneratorService reportGeneratorService;

    @Test
    @DisplayName("Empty test scoring produces 0.0% raw scores and report engine rejects it with IllegalArgumentException")
    void testEmptyTestScoringProducesZeroAndFailsFast() {
        User user = new User();
        user.setName("Empty Candidate");
        user.setEmail("empty." + System.currentTimeMillis() + "@test.com");
        user.setPassword("password123");
        user = userRepo.save(user);

        AssessmentAttempt attempt = new AssessmentAttempt();
        attempt.setCandidate(user);
        attempt.setCreatedBy(user);
        attempt.setAttemptToken("EMPTY-ATTEMPT-" + System.currentTimeMillis());
        attempt.setState(AttemptState.IN_PROGRESS);
        attempt.setCurrentBatteryIndex(0);
        attempt.setCreatedAt(Instant.now());

        List<BatterySession> sessions = new ArrayList<>();
        int seq = 1;
        for (BatteryType type : List.of(BatteryType.PQ10, BatteryType.DERAILERS, BatteryType.SJT, BatteryType.GCAT)) {
            BatterySession session = new BatterySession();
            session.setBatteryType(type);
            session.setAttempt(attempt);
            session.setSequenceOrder(seq++);
            session.setState(SessionState.SUBMITTED);
            session.setTimeLimitSeconds(1800);
            session.setSampledItemIds(new ArrayList<>());
            session.setResponses(new ArrayList<>()); // 0 answers
            sessions.add(session);
        }
        attempt.setBatterySessions(sessions);
        attempt = attemptRepo.save(attempt);

        // 1. Run through Scoring Service
        AssessmentScore score = scoringService.scoreAttempt(attempt);

        System.out.println("================================================================================");
        System.out.println("[EMPTY TEST SCORING AUDIT]");
        System.out.println("  Personality Score Pct: " + score.getPersonalityScorePct() + "%");
        System.out.println("  Derailers Effective Score Pct: " + score.getDerailersEffectiveScorePct() + "%");
        System.out.println("  SJT Score Pct: " + score.getSjtScorePct() + "%");
        System.out.println("  Cognitive Score Pct: " + score.getCognitiveScorePct() + "%");
        System.out.println("  Composite Score: " + score.getCompositeScore());
        System.out.println("================================================================================");

        // Verify all raw scores are exactly 0.0%
        assertEquals(0.0, score.getPersonalityScorePct(), 0.01);
        assertEquals(0.0, score.getDerailersEffectiveScorePct(), 0.01);
        assertEquals(0.0, score.getSjtScorePct(), 0.01);
        assertEquals(0.0, score.getCognitiveScorePct(), 0.01);
        assertEquals(0.0, score.getCompositeScore(), 0.01);

        // 2. Convert to DTO and verify report generation fails fast
        AssessmentScoreResponseDto dto = AssessmentScoreResponseDto.fromEntity(score);

        org.springframework.security.access.AccessDeniedException ex = assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            reportGeneratorService.generateReport(dto);
        });

        System.out.println("Report Service Rejection Message: " + ex.getMessage());
        assertEquals("لا تملك الصلاحية لإصدار التقرير: لم تقم باستكمال جميع بطاريات الاختبار.", ex.getMessage());
    }
}
