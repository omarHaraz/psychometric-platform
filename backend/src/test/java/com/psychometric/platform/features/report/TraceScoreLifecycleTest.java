package com.psychometric.platform.features.report;

import com.psychometric.platform.features.assessment.domain.model.AssessmentScore;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto;
import com.psychometric.platform.features.assessment.repository.AssessmentScoreRepository;
import com.psychometric.platform.features.report.dto.ReportContextDto;
import com.psychometric.platform.features.report.service.LeadershipReportGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("local")
public class TraceScoreLifecycleTest {

    @Autowired
    private AssessmentScoreRepository scoreRepository;

    @Autowired
    private LeadershipReportGeneratorService reportGeneratorService;

    @Test
    public void traceAttemptScores() {
        String token = "4a52866d-2f84-4b2f-abcd-d284fa1f23d8";
        AssessmentScore score = scoreRepository.findByAttemptAttemptToken(token).orElse(null);

        if (score == null) {
            System.out.println("No AssessmentScore found for token: " + token + ", checking any score in DB...");
            List<AssessmentScore> all = scoreRepository.findAll();
            if (!all.isEmpty()) {
                score = all.get(0);
                System.out.println("Using score with attempt token: " + (score.getAttempt() != null ? score.getAttempt().getAttemptToken() : "null"));
            } else {
                System.out.println("No scores in database at all.");
                return;
            }
        }

        AssessmentScoreResponseDto rawScoreDto = AssessmentScoreResponseDto.fromEntity(score);
        System.out.println("=== RAW TRAIT SCORES ===");
        if (rawScoreDto.getTraitScores() != null) {
            for (var ts : rawScoreDto.getTraitScores()) {
                System.out.println(String.format("Trait: %s | DisplayOrder: %s | ScorePct: %s", ts.getTraitCode(), ts.getDisplayOrder(), ts.getScorePct()));
            }
        }
        if (rawScoreDto.getGcatSubtestScores() != null) {
            System.out.println("=== RAW GCAT SCORES ===");
            for (var gs : rawScoreDto.getGcatSubtestScores()) {
                System.out.println(String.format("GCAT: %s | ScorePct: %s", gs.getSubtest(), gs.getScorePct()));
            }
        }

        ReportContextDto report = reportGeneratorService.generateReport(rawScoreDto);

        System.out.println("=== REPORT DTO PAGE 5 COLORS & SCORES ===");
        System.out.println("commScore: " + report.getCommScore() + " | commColor: " + report.getCommColor());
        System.out.println("initiativeScore: " + report.getInitiativeScore() + " | initiativeColor: " + report.getInitiativeColor());
        System.out.println("decisionScore: " + report.getDecisionScore() + " | decisionColor: " + report.getDecisionColor());
        System.out.println("leadershipScore: " + report.getLeadershipScore() + " | leadershipColor: " + report.getLeadershipColor());
        System.out.println("strategicScore: " + report.getStrategicScore() + " | strategicColor: " + report.getStrategicColor());
        System.out.println("skillsScore: " + report.getSkillsScore() + " | skillsColor: " + report.getSkillsColor());
        System.out.println("adaptabilityScore: " + report.getAdaptabilityScore() + " | adaptabilityColor: " + report.getAdaptabilityColor());
        System.out.println("analysisScore: " + report.getAnalysisScore() + " | analysisColor: " + report.getAnalysisColor());
        System.out.println("abstractScore: " + report.getAbstractScore() + " | abstractColor: " + report.getAbstractColor());
        System.out.println("numericalScore: " + report.getNumericalScore() + " | numericalColor: " + report.getNumericalColor());
        System.out.println("verbalScore: " + report.getVerbalScore() + " | verbalColor: " + report.getVerbalColor());

        System.out.println("=== DETAILED COMPETENCY PAGES 6-13 ===");
        for (int p = 6; p <= 13; p++) {
            var cp = report.getCompetencyPages().get(p);
            if (cp != null) {
                System.out.println(String.format("Page %d [%s]: Score=%.2f, Color=%s, Row1Color=%s, Row2Color=%s, Row3Color=%s",
                        p, cp.getCompetencyTitle(), cp.getCompetencyScore(), cp.getCompetencyColor(),
                        cp.getIndicator1Color(), cp.getIndicator2Color(), cp.getIndicator3Color()));
            } else {
                System.out.println(String.format("Page %d: NULL", p));
            }
        }
    }
}
