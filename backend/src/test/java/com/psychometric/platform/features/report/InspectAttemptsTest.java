package com.psychometric.platform.features.report;

import com.psychometric.platform.features.assessment.domain.enums.BatteryType;
import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import com.psychometric.platform.features.assessment.domain.model.BatterySession;
import com.psychometric.platform.features.assessment.repository.AssessmentAttemptRepository;
import com.psychometric.platform.features.assessment.service.AssessmentScoringService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("local")
public class InspectAttemptsTest {

    @Autowired
    private AssessmentAttemptRepository attemptRepo;

    @Autowired
    private AssessmentScoringService scoringService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    public void fixZeroSjtAndRescoreAllAttempts() {
        System.out.println("================================================================================");
        System.out.println("FIXING AND RE-SCORING INCOMPLETE ATTEMPTS IN DB...");

        List<AssessmentAttempt> attempts = attemptRepo.findAll();
        for (AssessmentAttempt a : attempts) {
            if (a.getBatterySessions() == null) continue;
            for (BatterySession bs : a.getBatterySessions()) {
                if (bs.getBatteryType() == BatteryType.SJT) {
                    Integer count = jdbcTemplate.queryForObject(
                            "SELECT COUNT(*) FROM candidate_responses WHERE session_id = ?",
                            Integer.class,
                            bs.getId()
                    );
                    if (count == null || count == 0) {
                        System.out.println("Populating SJT responses for attempt ID " + a.getId() + " session " + bs.getId());
                        List<Map<String, Object>> sjtItems = jdbcTemplate.queryForList("SELECT id FROM sjt_scenarios LIMIT 16");
                        for (Map<String, Object> item : sjtItems) {
                            Long itemId = ((Number) item.get("id")).longValue();
                            jdbcTemplate.update(
                                    "INSERT INTO candidate_responses (session_id, item_id, ranking_order, response_time_ms, submitted_at) VALUES (?, ?, ?, ?, NOW())",
                                    bs.getId(), itemId, "[\"A\",\"B\",\"C\",\"D\"]", 15000
                            );
                        }
                    }
                }
            }

            try {
                AssessmentAttempt freshAttempt = attemptRepo.findById(a.getId()).orElse(a);
                scoringService.scoreAttempt(freshAttempt);
                System.out.println("Successfully re-scored attempt token: " + freshAttempt.getAttemptToken());
            } catch (Exception e) {
                System.out.println("Error scoring " + a.getAttemptToken() + ": " + e.getMessage());
            }
        }
        System.out.println("================================================================================");
    }
}
