package com.psychometric.platform.features.report;

import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto.TraitScoreDto;
import com.psychometric.platform.features.report.dto.ReportContextDto;
import com.psychometric.platform.features.report.service.LeadershipReportGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmptyAssessmentValidationAndZeroFallbackTest {

    private LeadershipReportGeneratorService service;

    @BeforeEach
    void setUp() {
        service = new LeadershipReportGeneratorService(new com.fasterxml.jackson.databind.ObjectMapper(), null, null);
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when rawScore is null")
    void testNullRawScoreThrowsException() {
        AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> {
            service.generateReport(null);
        });
        assertEquals("لا تملك الصلاحية لإصدار التقرير: لم تقم باستكمال جميع بطاريات الاختبار.", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when only 2 out of 3 batteries are completed (Missing Derailers)")
    void testMissingDerailersThrowsAccessDeniedException() {
        AssessmentScoreResponseDto dto = new AssessmentScoreResponseDto();
        dto.setPersonalityScorePct(80.0);
        dto.setCognitiveScorePct(75.0);
        dto.setDerailersEffectiveScorePct(0.0); // Missing Derailers
        dto.setCompositeScore(60.0);
        dto.setTraitScores(List.of(new TraitScoreDto("COMMUNICATION_AND_INFLUENCE", "التواصل والتأثير", 1, 80.0, 80.0)));

        AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> {
            service.generateReport(dto);
        });
        assertEquals("لا تملك الصلاحية لإصدار التقرير: لم تقم باستكمال جميع بطاريات الاختبار.", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when only 2 out of 3 batteries are completed (Missing GCAT)")
    void testMissingGcatThrowsAccessDeniedException() {
        AssessmentScoreResponseDto dto = new AssessmentScoreResponseDto();
        dto.setPersonalityScorePct(80.0);
        dto.setDerailersEffectiveScorePct(75.0);
        dto.setCognitiveScorePct(0.0); // Missing GCAT
        dto.setCompositeScore(60.0);
        dto.setTraitScores(List.of(new TraitScoreDto("COMMUNICATION_AND_INFLUENCE", "التواصل والتأثير", 1, 80.0, 80.0)));

        AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> {
            service.generateReport(dto);
        });
        assertEquals("لا تملك الصلاحية لإصدار التقرير: لم تقم باستكمال جميع بطاريات الاختبار.", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when only 2 out of 3 batteries are completed (Missing Personality)")
    void testMissingPersonalityThrowsAccessDeniedException() {
        AssessmentScoreResponseDto dto = new AssessmentScoreResponseDto();
        dto.setPersonalityScorePct(0.0); // Missing Personality
        dto.setDerailersEffectiveScorePct(75.0);
        dto.setCognitiveScorePct(70.0);
        dto.setCompositeScore(50.0);
        dto.setTraitScores(new ArrayList<>());

        AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> {
            service.generateReport(dto);
        });
        assertEquals("لا تملك الصلاحية لإصدار التقرير: لم تقم باستكمال جميع بطاريات الاختبار.", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when traitScores list is empty or null")
    void testEmptyTraitScoresThrowsException() {
        AssessmentScoreResponseDto dto = new AssessmentScoreResponseDto();
        dto.setPersonalityScorePct(80.0);
        dto.setDerailersEffectiveScorePct(75.0);
        dto.setCognitiveScorePct(70.0);
        dto.setCompositeScore(75.0);
        dto.setTraitScores(new ArrayList<>()); // Empty traits

        AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> {
            service.generateReport(dto);
        });
        assertEquals("لا تملك الصلاحية لإصدار التقرير: لم تقم باستكمال جميع بطاريات الاختبار.", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when compositeScore is 0.0 or negative")
    void testZeroCompositeScoreThrowsException() {
        AssessmentScoreResponseDto dto = new AssessmentScoreResponseDto();
        dto.setPersonalityScorePct(80.0);
        dto.setDerailersEffectiveScorePct(75.0);
        dto.setCognitiveScorePct(70.0);
        dto.setCompositeScore(0.0);
        dto.setTraitScores(List.of(new TraitScoreDto("COMMUNICATION_AND_INFLUENCE", "التواصل والتأثير", 1, 80.0, 80.0)));

        AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> {
            service.generateReport(dto);
        });
        assertEquals("لا تملك الصلاحية لإصدار التقرير: لم تقم باستكمال جميع بطاريات الاختبار.", ex.getMessage());
    }

    @Test
    @DisplayName("Should scale missing individual trait scores to 1.0 (not 3.0) and color Red (#d32f2f) when all batteries are valid")
    void testMissingTraitScalesTo1Point0Not3Point0() {
        AssessmentScoreResponseDto dto = new AssessmentScoreResponseDto();
        dto.setCandidateName("Test Candidate");
        dto.setAttemptToken("TEST-TOKEN-123");
        dto.setPersonalityScorePct(75.0);
        dto.setDerailersEffectiveScorePct(65.0);
        dto.setCognitiveScorePct(70.0);
        dto.setCompositeScore(68.0);
        dto.setPercentile(70);
        dto.setSocialDesirabilityRiskPct(15.0);
        dto.setCentralTendencyRatePct(10.0);

        // Only provide 1 trait (COMMUNICATION_AND_INFLUENCE = 75%), all other 7 traits are missing
        List<TraitScoreDto> traitScores = new ArrayList<>();
        traitScores.add(new TraitScoreDto("COMMUNICATION_AND_INFLUENCE", "التواصل والتأثير", 1, 75.0, 75.0));
        dto.setTraitScores(traitScores);

        ReportContextDto report = service.generateReport(dto);
        assertNotNull(report);

        // Provided trait (75% -> 1.0 + (0.75 * 4.0) = 4.0 -> Green #388e3c)
        assertEquals(4.0, report.getCommScore(), 0.01);
        assertEquals("#388e3c", report.getCommColor());

        // Missing traits must scale to 1.0 (NOT 3.0), and color must be RED #d32f2f
        assertEquals(1.0, report.getInitiativeScore(), 0.01, "Missing INITIATIVE must scale to 1.0, not 3.0");
        assertEquals("#d32f2f", report.getInitiativeColor(), "Missing INITIATIVE color must be Red (#d32f2f)");

        assertEquals(1.0, report.getDecisionScore(), 0.01, "Missing DECISION must scale to 1.0, not 3.0");
        assertEquals("#d32f2f", report.getDecisionColor(), "Missing DECISION color must be Red (#d32f2f)");

        assertEquals(1.0, report.getLeadershipScore(), 0.01, "Missing LEADERSHIP must scale to 1.0, not 3.0");
        assertEquals("#d32f2f", report.getLeadershipColor(), "Missing LEADERSHIP color must be Red (#d32f2f)");

        assertEquals(1.0, report.getStrategicScore(), 0.01, "Missing STRATEGIC must scale to 1.0, not 3.0");
        assertEquals("#d32f2f", report.getStrategicColor(), "Missing STRATEGIC color must be Red (#d32f2f)");

        assertEquals(1.0, report.getSkillsScore(), 0.01, "Missing SKILLS must scale to 1.0, not 3.0");
        assertEquals("#d32f2f", report.getSkillsColor(), "Missing SKILLS color must be Red (#d32f2f)");

        assertEquals(1.0, report.getAdaptabilityScore(), 0.01, "Missing ADAPTABILITY must scale to 1.0, not 3.0");
        assertEquals("#d32f2f", report.getAdaptabilityColor(), "Missing ADAPTABILITY color must be Red (#d32f2f)");

        assertEquals(1.0, report.getAnalysisScore(), 0.01, "Missing ANALYSIS must scale to 1.0, not 3.0");
        assertEquals("#d32f2f", report.getAnalysisColor(), "Missing ANALYSIS color must be Red (#d32f2f)");

        // Missing cognitive subtests must scale to 1.0 and color RED
        assertEquals(1.0, report.getAbstractScore(), 0.01, "Missing ABSTRACT must scale to 1.0");
        assertEquals("#d32f2f", report.getAbstractColor());

        assertEquals(1.0, report.getNumericalScore(), 0.01, "Missing NUMERICAL must scale to 1.0");
        assertEquals("#d32f2f", report.getNumericalColor());

        assertEquals(1.0, report.getVerbalScore(), 0.01, "Missing VERBAL must scale to 1.0");
        assertEquals("#d32f2f", report.getVerbalColor());

        // Missing derailers must scale to 1 (Low Risk / 0% default)
        assertEquals(1, report.getReservedScore(), "Missing Reserved derailer score must default to 1");
        assertEquals(1, report.getEmotionalityScore(), "Missing Emotionality derailer score must default to 1");
        assertEquals(1, report.getHostilityScore(), "Missing Hostility derailer score must default to 1");
        assertEquals(1, report.getImpulsivityScore(), "Missing Impulsivity derailer score must default to 1");
        assertEquals(1, report.getRigidityScore(), "Missing Rigidity derailer score must default to 1");
        assertEquals(1, report.getUnconventionalityScore(), "Missing Unconventionality derailer score must default to 1");
    }
}
