package com.psychometric.platform.features.assessment;

import com.psychometric.platform.features.assessment.domain.enums.AttemptState;
import com.psychometric.platform.features.assessment.domain.enums.BatteryType;
import com.psychometric.platform.features.assessment.domain.enums.ReadinessBand;
import com.psychometric.platform.features.assessment.domain.model.*;
import com.psychometric.platform.features.assessment.repository.*;
import com.psychometric.platform.features.assessment.service.AssessmentScoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentScoringServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AssessmentScoreRepository assessmentScoreRepo;

    @Mock
    private CompetencyTraitRepository traitRepo;

    @Mock
    private DerailerCategoryRepository categoryRepo;

    @Mock
    private AssessmentAttemptRepository attemptRepo;

    private AssessmentScoringService scoringService;

    @BeforeEach
    void setUp() {
        scoringService = new AssessmentScoringService(
                jdbcTemplate,
                assessmentScoreRepo,
                traitRepo,
                categoryRepo,
                attemptRepo
        );
    }

    @Test
    @DisplayName("Verify ReadinessBand classification thresholds")
    void testReadinessBandClassification() {
        assertEquals(ReadinessBand.EXCELLENT, ReadinessBand.fromCompositeScore(100.0));
        assertEquals(ReadinessBand.EXCELLENT, ReadinessBand.fromCompositeScore(90.0));
        assertEquals(ReadinessBand.STRONG, ReadinessBand.fromCompositeScore(89.99));
        assertEquals(ReadinessBand.STRONG, ReadinessBand.fromCompositeScore(80.0));
        assertEquals(ReadinessBand.ACCEPTABLE, ReadinessBand.fromCompositeScore(79.99));
        assertEquals(ReadinessBand.ACCEPTABLE, ReadinessBand.fromCompositeScore(70.0));
        assertEquals(ReadinessBand.FOUNDATIONAL_ADVANCED, ReadinessBand.fromCompositeScore(69.99));
        assertEquals(ReadinessBand.FOUNDATIONAL_ADVANCED, ReadinessBand.fromCompositeScore(50.0));
        assertEquals(ReadinessBand.FOUNDATIONAL, ReadinessBand.fromCompositeScore(49.99));
        assertEquals(ReadinessBand.FOUNDATIONAL, ReadinessBand.fromCompositeScore(0.0));
    }

    @Test
    @DisplayName("Verify Logistic Percentile calculation")
    void testLogisticPercentile() {
        // At composite = 75.0, exp(0) = 1 -> 100 / (1 + 1) = 50th percentile
        int medianP = AssessmentScoringService.calculateLogisticPercentile(75.0);
        assertEquals(50, medianP);

        // At high composite (e.g. 90.0) -> high percentile (> 90)
        int highP = AssessmentScoringService.calculateLogisticPercentile(90.0);
        assertTrue(highP >= 90);

        // At low composite (e.g. 50.0) -> low percentile (< 10)
        int lowP = AssessmentScoringService.calculateLogisticPercentile(50.0);
        assertTrue(lowP <= 5);
    }

    @Test
    @DisplayName("Verify SJT Pairwise Concordance scoring with ties excluded")
    void testSjtScoringWithTies() {
        BatterySession sjtSession = new BatterySession();
        sjtSession.setBatteryType(BatteryType.SJT);
        sjtSession.setSampledItemIds(List.of(101L));

        CandidateResponse resp = new CandidateResponse();
        resp.setItemId(101L);
        // Candidate ranked: A, B, C, D
        resp.setRankingOrder(List.of("A", "B", "C", "D"));
        sjtSession.setResponses(List.of(resp));

        // Mock JDBC for sjt_options:
        // A = 5.0, B = 4.0, C = 4.0 (Tie with B), D = 2.0
        // Pairs:
        // (A,B): Expert A>B (5>4), Candidate A>B -> Concordant
        // (A,C): Expert A>C (5>4), Candidate A>C -> Concordant
        // (A,D): Expert A>D (5>2), Candidate A>D -> Concordant
        // (B,C): Expert B=C (4=4) -> TIE! (Excluded)
        // (B,D): Expert B>D (4>2), Candidate B>D -> Concordant
        // (C,D): Expert C>D (4>2), Candidate C>D -> Concordant
        // Total valid pairs: 5. Total concordant: 5. Score = 100.0%
        doAnswer(invocation -> {
            RowCallbackHandler handler = invocation.getArgument(1);
            var rs = mock(java.sql.ResultSet.class);
            when(rs.getLong("scenario_id")).thenReturn(101L, 101L, 101L, 101L);
            when(rs.getString("option_key")).thenReturn("A", "B", "C", "D");
            when(rs.getDouble("effectiveness_score")).thenReturn(5.0, 4.0, 4.0, 2.0);

            handler.processRow(rs);
            handler.processRow(rs);
            handler.processRow(rs);
            handler.processRow(rs);
            return null;
        }).when(jdbcTemplate).query(anyString(), any(RowCallbackHandler.class), any());

        double sjtScore = scoringService.scoreSjt(sjtSession);
        assertEquals(100.0, sjtScore, 0.001);
    }

    @Test
    @DisplayName("Verify end-to-end score calculation and persistence")
    void testScoreAttemptEndToEnd() {
        AssessmentAttempt attempt = new AssessmentAttempt();
        attempt.setId(1L);
        attempt.setAttemptToken("test-token-123");

        // Sessions
        BatterySession s0 = new BatterySession();
        s0.setBatteryType(BatteryType.PQ10);
        s0.setSequenceOrder(0);

        BatterySession s1 = new BatterySession();
        s1.setBatteryType(BatteryType.SJT);
        s1.setSequenceOrder(1);

        BatterySession s2 = new BatterySession();
        s2.setBatteryType(BatteryType.DERAILERS);
        s2.setSequenceOrder(2);

        BatterySession s3 = new BatterySession();
        s3.setBatteryType(BatteryType.GCAT);
        s3.setSequenceOrder(3);

        attempt.setBatterySessions(List.of(s0, s1, s2, s3));

        // Mock CompetencyTraits & Categories
        CompetencyTrait t1 = new CompetencyTrait("COMMUNICATION_AND_INFLUENCE", "التواصل", "تعريف", 1);
        t1.setId(1L);
        when(traitRepo.findAllByOrderByDisplayOrderAsc()).thenReturn(List.of(t1));

        DerailerCategory c1 = new DerailerCategory("التحفظ", "تعريف", List.of("مؤشر"), 1);
        c1.setId(1L);
        when(categoryRepo.findAllByOrderByDisplayOrderAsc()).thenReturn(List.of(c1));

        when(assessmentScoreRepo.findByAttemptId(1L)).thenReturn(Optional.empty());
        when(assessmentScoreRepo.save(any(AssessmentScore.class))).thenAnswer(i -> i.getArgument(0));

        AssessmentScore score = scoringService.scoreAttempt(attempt);

        assertNotNull(score);
        assertEquals(attempt, score.getAttempt());
        assertEquals(AttemptState.SCORED, attempt.getState());
        assertNotNull(score.getScoredAt());
        assertNotNull(score.getReadinessBand());
        verify(assessmentScoreRepo, times(1)).save(any(AssessmentScore.class));
        verify(attemptRepo, times(1)).save(attempt);
    }

    @Test
    @DisplayName("Verify PQ10 per-trait mathematical invariants (rawScore <= n*4 and exact percentage)")
    void testPq10PerTraitBreakdownMathematicalInvariants() {
        // Setup 8 traits
        List<CompetencyTrait> allTraits = new ArrayList<>();
        String[] codes = {
            "COMMUNICATION_AND_INFLUENCE", "INITIATIVE", "DECISION_MAKING_AND_RESPONSIBILITY", "INSPIRING_LEADERSHIP",
            "STRATEGIC_THINKING", "SKILL_DEVELOPMENT", "ADAPTABILITY", "SYSTEMATIC_ANALYSIS_AND_PLANNING"
        };
        for (int i = 1; i <= 8; i++) {
            CompetencyTrait t = new CompetencyTrait(codes[i - 1], "Trait " + i, "Def " + i, i);
            t.setId((long) i);
            allTraits.add(t);
        }
        when(traitRepo.findAllByOrderByDisplayOrderAsc()).thenReturn(allTraits);

        // Setup 140 sampled items, each mapped to 2 competencies (like the real item bank)
        List<Long> sampledIds = new ArrayList<>();
        List<CandidateResponse> responses = new ArrayList<>();
        Map<Long, Integer> targetMap = new HashMap<>();

        // Generate 140 items (18 each for traits 1-4, 17 each for traits 5-8)
        long itemIdCounter = 1L;
        List<Object[]> dbRows = new ArrayList<>();

        for (int traitId = 1; traitId <= 8; traitId++) {
            int quota = traitId <= 4 ? 18 : 17;
            for (int k = 0; k < quota; k++) {
                long itemId = itemIdCounter++;
                sampledIds.add(itemId);
                targetMap.put(itemId, 5);

                // Map to primary trait and secondary trait
                int secondaryTrait = (traitId % 8) + 1;
                dbRows.add(new Object[]{itemId, 5, (long) traitId});
                dbRows.add(new Object[]{itemId, 5, (long) secondaryTrait});

                // Simulate candidate response (e.g. 4 on 5-point scale -> 1 distance -> 3 points)
                CandidateResponse cr = new CandidateResponse();
                cr.setItemId(itemId);
                cr.setSelectedLikert(4);
                responses.add(cr);
            }
        }

        assertEquals(140, sampledIds.size());

        // Mock JDBC query
        doAnswer(invocation -> {
            RowCallbackHandler handler = invocation.getArgument(1);
            for (Object[] row : dbRows) {
                var rs = mock(java.sql.ResultSet.class);
                when(rs.getLong("id")).thenReturn((Long) row[0]);
                when(rs.getInt("ideal_target")).thenReturn((Integer) row[1]);
                when(rs.getLong("competency_id")).thenReturn((Long) row[2]);
                handler.processRow(rs);
            }
            return null;
        }).when(jdbcTemplate).query(anyString(), any(RowCallbackHandler.class));

        BatterySession pq10Session = new BatterySession();
        pq10Session.setBatteryType(BatteryType.PQ10);
        pq10Session.setSampledItemIds(sampledIds);
        pq10Session.setResponses(responses);

        AssessmentAttempt attempt = new AssessmentAttempt();
        attempt.setId(99L);
        attempt.setBatterySessions(List.of(pq10Session));

        when(assessmentScoreRepo.findByAttemptId(99L)).thenReturn(Optional.empty());
        when(assessmentScoreRepo.save(any(AssessmentScore.class))).thenAnswer(i -> i.getArgument(0));

        AssessmentScore score = scoringService.scoreAttempt(attempt);

        assertNotNull(score);
        Set<TraitScore> traitScores = score.getTraitScores();
        assertEquals(8, traitScores.size());

        double sumRawScores = 0.0;

        for (TraitScore ts : traitScores) {
            CompetencyTrait trait = ts.getTrait();
            int n = trait.getDisplayOrder() <= 4 ? 18 : 17;
            double maxPoints = n * 4.0;

            // Invariant 1: rawScore <= maxPoints (72 for traits 1-4, 68 for traits 5-8)
            assertTrue(ts.getRawScore() <= maxPoints,
                    String.format("Trait %s raw score %.2f exceeds max ceiling %.2f (n=%d)",
                            trait.getCode(), ts.getRawScore(), maxPoints, n));

            // Invariant 2: scorePct == rawScore / (n * 4) * 100
            double expectedPct = Math.round((ts.getRawScore() / maxPoints) * 100.0 * 100.0) / 100.0;
            assertEquals(expectedPct, ts.getScorePct(), 0.01,
                    String.format("Trait %s score percentage %.2f does not match expected %.2f (raw=%.2f, max=%.2f)",
                            trait.getCode(), ts.getScorePct(), expectedPct, ts.getRawScore(), maxPoints));

            // In our test vector: each item has answer=4, target=5 -> 3 points each
            assertEquals(n * 3.0, ts.getRawScore(), 0.01);
            assertEquals(75.0, ts.getScorePct(), 0.01);

            sumRawScores += ts.getRawScore();
        }

        // Invariant 3: Total sum of raw points across all 8 traits == 140 * 3 = 420
        assertEquals(140 * 3.0, sumRawScores, 0.01);

        // Invariant 4: Overall PQ10% == 420 / 560 * 100 == 75.0%
        assertEquals(75.0, score.getPersonalityScorePct(), 0.01);
    }
}
