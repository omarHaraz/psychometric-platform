package com.psychometric.platform.features.assessment.service;

import com.psychometric.platform.features.assessment.domain.enums.AttemptState;
import com.psychometric.platform.features.assessment.domain.enums.BatteryType;
import com.psychometric.platform.features.assessment.domain.enums.ReadinessBand;
import com.psychometric.platform.features.assessment.domain.model.*;
import com.psychometric.platform.features.assessment.repository.*;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssessmentScoringService {

    private static final Logger log = LoggerFactory.getLogger(AssessmentScoringService.class);

    private final JdbcTemplate jdbcTemplate;
    private final AssessmentScoreRepository assessmentScoreRepo;
    private final CompetencyTraitRepository traitRepo;
    private final DerailerCategoryRepository categoryRepo;
    private final AssessmentAttemptRepository attemptRepo;

    @Value("${psychometric.scoring.validity.central-tendency-cutoff-pct:45.0}")
    private double centralTendencyCutoffPct = 45.0;

    @Value("${psychometric.scoring.validity.sd-penalty-threshold-pct:60.0}")
    private double sdPenaltyThresholdPct = 60.0;

    @Value("${psychometric.scoring.validity.sd-penalty-rate:0.15}")
    private double sdPenaltyRate = 0.15;

    @Value("${psychometric.scoring.validity.ct-penalty-threshold-pct:45.0}")
    private double ctPenaltyThresholdPct = 45.0;

    @Value("${psychometric.scoring.validity.ct-penalty-rate:0.15}")
    private double ctPenaltyRate = 0.15;

    @Value("${psychometric.scoring.validity.max-penalty-cap-pct:15.0}")
    private double maxPenaltyCapPct = 15.0;

    public AssessmentScoringService(JdbcTemplate jdbcTemplate,
                                    AssessmentScoreRepository assessmentScoreRepo,
                                    CompetencyTraitRepository traitRepo,
                                    DerailerCategoryRepository categoryRepo,
                                    AssessmentAttemptRepository attemptRepo) {
        this.jdbcTemplate = jdbcTemplate;
        this.assessmentScoreRepo = assessmentScoreRepo;
        this.traitRepo = traitRepo;
        this.categoryRepo = categoryRepo;
        this.attemptRepo = attemptRepo;
    }

    public void setCentralTendencyCutoffPct(double centralTendencyCutoffPct) {
        this.centralTendencyCutoffPct = centralTendencyCutoffPct;
    }

    public void setSdPenaltyThresholdPct(double sdPenaltyThresholdPct) {
        this.sdPenaltyThresholdPct = sdPenaltyThresholdPct;
    }

    public void setSdPenaltyRate(double sdPenaltyRate) {
        this.sdPenaltyRate = sdPenaltyRate;
    }

    public void setCtPenaltyThresholdPct(double ctPenaltyThresholdPct) {
        this.ctPenaltyThresholdPct = ctPenaltyThresholdPct;
    }

    public void setCtPenaltyRate(double ctPenaltyRate) {
        this.ctPenaltyRate = ctPenaltyRate;
    }

    public void setMaxPenaltyCapPct(double maxPenaltyCapPct) {
        this.maxPenaltyCapPct = maxPenaltyCapPct;
    }

    /**
     * Scores all 4 batteries for an attempt, computes the overall composite score,
     * percentile, and promotion readiness band, and persists the AssessmentScore.
     */
    @Transactional
    public AssessmentScore scoreAttempt(AssessmentAttempt attempt) {
        log.info("Starting scoring calculation for attempt ID: {}", attempt.getId());

        List<BatterySession> sessions = attempt.getBatterySessions();
        if (sessions == null || sessions.isEmpty()) {
            throw new IllegalStateException("Attempt has no battery sessions to score.");
        }

        // 1. Score PQ10
        BatterySession pq10Session = sessions.stream()
                .filter(s -> s.getBatteryType() == BatteryType.PQ10)
                .findFirst()
                .orElse(null);
        Pq10ScoreResult pq10Result = scorePq10(pq10Session);

        // 2. Score Derailers
        BatterySession derailerSession = sessions.stream()
                .filter(s -> s.getBatteryType() == BatteryType.DERAILERS)
                .findFirst()
                .orElse(null);
        DerailerScoreResult derailerResult = scoreDerailers(derailerSession);

        // 3. Score SJT
        BatterySession sjtSession = sessions.stream()
                .filter(s -> s.getBatteryType() == BatteryType.SJT)
                .findFirst()
                .orElse(null);
        double sjtScorePct = scoreSjt(sjtSession);

        // 4. Score GCAT
        BatterySession gcatSession = sessions.stream()
                .filter(s -> s.getBatteryType() == BatteryType.GCAT)
                .findFirst()
                .orElse(null);
        GcatScoreResult gcatResult = scoreGcat(gcatSession);

        // 5. Compute Central Tendency Index (مؤشر الوسطية) across 196 substantive items (136 PQ10 non-SD + 60 Derailers)
        int midpointCount = 0;
        int totalSubstantiveItems = 0;

        if (pq10Session != null && pq10Session.getResponses() != null) {
            for (CandidateResponse cr : pq10Session.getResponses()) {
                if (cr.getItemId() != null && cr.getSelectedLikert() != null && !pq10Result.sdItemIds.contains(cr.getItemId())) {
                    totalSubstantiveItems++;
                    if (cr.getSelectedLikert() == 3) {
                        midpointCount++;
                    }
                }
            }
        }

        if (derailerSession != null && derailerSession.getResponses() != null) {
            for (CandidateResponse cr : derailerSession.getResponses()) {
                if (cr.getItemId() != null && cr.getSelectedLikert() != null) {
                    totalSubstantiveItems++;
                    if (cr.getSelectedLikert() == 3) {
                        midpointCount++;
                    }
                }
            }
        }

        double centralTendencyPct = (totalSubstantiveItems > 0)
                ? ((double) midpointCount / totalSubstantiveItems) * 100.0
                : 0.0;
        boolean elevatedCentralTendency = (centralTendencyPct >= centralTendencyCutoffPct);

        // 6. Raw Composite Score Calculation
        // Raw Composite = 0.28*PQ10% + 0.22*SJT% + 0.20*Derailers% + 0.30*GCAT%
        double rawComposite = (0.28 * pq10Result.overallPct)
                            + (0.22 * sjtScorePct)
                            + (0.20 * derailerResult.overallPct)
                            + (0.30 * gcatResult.overallPct);

        // 7. Validity Penalty Calculation
        // ValidityPenaltyPct = max(0, SDRiskPct - 60) * 0.15 + max(0, CentralTendencyRatePct - 45) * 0.15
        double sdExcess = Math.max(0.0, pq10Result.sdRiskPct - sdPenaltyThresholdPct);
        double ctExcess = Math.max(0.0, centralTendencyPct - ctPenaltyThresholdPct);
        double validityPenalty = (sdExcess * sdPenaltyRate) + (ctExcess * ctPenaltyRate);

        // Hard Cap: CappedPenalty = min(ValidityPenaltyPct, 15)
        double cappedPenalty = Math.min(validityPenalty, maxPenaltyCapPct);

        // Adjusted Composite = max(0.0, RawComposite - CappedPenalty)
        double adjustedComposite = Math.max(0.0, rawComposite - cappedPenalty);

        // Percentile & Readiness Band based on Adjusted Composite
        // Percentile = round[ 100 / (1 + exp(-(AdjustedComposite - 75.0) / 6.0)) ]
        int percentile = calculateLogisticPercentile(adjustedComposite);

        // Promotion Readiness Band
        ReadinessBand band = ReadinessBand.fromCompositeScore(adjustedComposite);

        // Persist AssessmentScore
        AssessmentScore score = assessmentScoreRepo.findByAttemptId(attempt.getId())
                .orElse(new AssessmentScore());
        score.setAttempt(attempt);
        score.setPersonalityScorePct(round2(pq10Result.overallPct));
        score.setSjtScorePct(round2(sjtScorePct));
        score.setDerailersEffectiveScorePct(round2(derailerResult.overallPct));
        score.setCognitiveScorePct(round2(gcatResult.overallPct));
        score.setRawCompositeScore(round2(rawComposite));
        score.setValidityPenaltyPct(round2(validityPenalty));
        score.setCappedPenaltyPct(round2(cappedPenalty));
        score.setCompositeScore(round2(adjustedComposite));
        score.setPercentile(percentile);
        score.setReadinessBand(band);
        score.setSocialDesirabilityRiskPct(round2(pq10Result.sdRiskPct));
        score.setElevatedImpressionManagement(pq10Result.elevatedImpressionManagement);
        score.setCentralTendencyRatePct(round2(centralTendencyPct));
        score.setElevatedCentralTendency(elevatedCentralTendency);
        score.setScoredAt(Instant.now());

        // Attach Trait Scores
        score.getTraitScores().clear();
        for (TraitScore ts : pq10Result.traitScores) {
            ts.setAssessmentScore(score);
            score.getTraitScores().add(ts);
        }

        // Attach Derailer Category Scores
        score.getDerailerCategoryScores().clear();
        for (DerailerCategoryScore dcs : derailerResult.categoryScores) {
            dcs.setAssessmentScore(score);
            score.getDerailerCategoryScores().add(dcs);
        }

        // Attach GCAT Subtest Scores
        score.getGcatSubtestScores().clear();
        for (GcatSubtestScore gss : gcatResult.subtestScores) {
            gss.setAssessmentScore(score);
            score.getGcatSubtestScores().add(gss);
        }

        AssessmentScore savedScore = assessmentScoreRepo.save(score);

        attempt.setState(AttemptState.SCORED);
        attemptRepo.save(attempt);

        log.info("Scoring complete for attempt ID: {}. Composite: {}, Percentile: {}, Band: {}",
                attempt.getId(), score.getCompositeScore(), score.getPercentile(), score.getReadinessBand());

        return savedScore;
    }

    // =========================================================================
    // 1. PQ10 Scoring (Distance = |A_i - T_i|, Points = 4 - Distance) & Social Desirability
    // =========================================================================
    public static class Pq10ScoreResult {
        public double overallPct;
        public double sdRiskPct;
        public boolean elevatedImpressionManagement;
        public Set<Long> sdItemIds = new HashSet<>();
        public List<TraitScore> traitScores = new ArrayList<>();
    }

    private Pq10ScoreResult scorePq10(BatterySession session) {
        Pq10ScoreResult result = new Pq10ScoreResult();
        if (session == null) return result;

        List<CandidateResponse> responses = session.getResponses();
        Map<Long, Integer> answerMap = new HashMap<>();
        if (responses != null) {
            for (CandidateResponse cr : responses) {
                if (cr.getItemId() != null && cr.getSelectedLikert() != null) {
                    answerMap.put(cr.getItemId(), cr.getSelectedLikert());
                }
            }
        }

        List<CompetencyTrait> allTraits = traitRepo.findAllByOrderByDisplayOrderAsc();

        // Identify social desirability item IDs
        Set<Long> sdItemIds = new HashSet<>(jdbcTemplate.queryForList(
                "SELECT DISTINCT pic.item_id FROM personality_item_competencies pic " +
                "JOIN competencies c ON pic.competency_id = c.id " +
                "WHERE c.code = 'SOCIAL_DESIRABILITY'",
                Long.class
        ));
        result.sdItemIds = sdItemIds;

        // Query all items and their target answers and all their mapped competencies
        Map<Long, Integer> targetMap = new HashMap<>();
        Map<Long, Set<Long>> itemToTraitsMap = new HashMap<>();

        jdbcTemplate.query(
                "SELECT pi.id, pi.ideal_target, pic.competency_id " +
                "FROM personality_items pi " +
                "JOIN personality_item_competencies pic ON pi.id = pic.item_id",
                rs -> {
                    long itemId = rs.getLong("id");
                    int target = rs.getInt("ideal_target");
                    long traitId = rs.getLong("competency_id");
                    targetMap.put(itemId, target > 0 ? target : 5);
                    itemToTraitsMap.computeIfAbsent(itemId, k -> new HashSet<>()).add(traitId);
                }
        );

        List<Long> sampledIds = session.getSampledItemIds();
        if (sampledIds == null || sampledIds.isEmpty()) {
            sampledIds = new ArrayList<>(answerMap.keySet());
        }

        // Separate 136 competency items and 4 social desirability items
        List<Long> competencySampledIds = new ArrayList<>();
        List<Long> sdSampledIds = new ArrayList<>();
        for (Long id : sampledIds) {
            if (sdItemIds.contains(id)) {
                sdSampledIds.add(id);
            } else {
                competencySampledIds.add(id);
            }
        }

        // Partition the 136 competency items into the 8 traits (17 items each) using bipartite matching
        Map<Long, List<Long>> traitAssigned = partitionItemsByTrait(competencySampledIds, itemToTraitsMap, allTraits);

        double totalPoints = 0.0;

        for (CompetencyTrait trait : allTraits) {
            List<Long> traitItemIds = traitAssigned.getOrDefault(trait.getId(), Collections.emptyList());
            int n = 17; // Exactly 17 items for each of the 8 competency traits

            double raw = 0.0;
            for (Long itemId : traitItemIds) {
                int answer = answerMap.getOrDefault(itemId, 3);
                int target = targetMap.getOrDefault(itemId, 5);
                int distance = Math.abs(answer - target);
                int points = 4 - distance;
                raw += points;
            }

            totalPoints += raw;

            double maxPossiblePoints = n * 4.0; // 68.0
            double pct = (maxPossiblePoints > 0) ? (raw / maxPossiblePoints) * 100.0 : 0.0;

            result.traitScores.add(new TraitScore(null, trait, round2(raw), round2(pct)));
        }

        // overallPct across 136 competency items (max 544.0 points)
        result.overallPct = (totalPoints / 544.0) * 100.0;

        // Score Social Desirability validity category: SDRiskPct = (Σ(A_i - 1) / (4 * 4)) * 100
        double sdSum = 0.0;
        int sdCount = sdSampledIds.isEmpty() ? 4 : sdSampledIds.size();
        for (Long itemId : sdSampledIds) {
            int answer = answerMap.getOrDefault(itemId, 1);
            sdSum += (answer - 1);
        }
        double sdRiskPct = (sdSum / (sdCount * 4.0)) * 100.0;
        result.sdRiskPct = Math.min(100.0, Math.max(0.0, sdRiskPct));
        result.elevatedImpressionManagement = (result.sdRiskPct >= 60.0);

        return result;
    }

    private Map<Long, List<Long>> partitionItemsByTrait(List<Long> sampledIds,
                                                        Map<Long, Set<Long>> itemToTraitsMap,
                                                        List<CompetencyTrait> allTraits) {
        Map<Long, Integer> traitQuotas = new HashMap<>();
        for (CompetencyTrait t : allTraits) {
            traitQuotas.put(t.getId(), 17); // Exactly 17 items for each trait
        }

        Map<Long, List<Long>> traitAssigned = new HashMap<>();
        for (CompetencyTrait t : allTraits) {
            traitAssigned.put(t.getId(), new ArrayList<>());
        }

        // Sort items so items with fewer candidate traits are matched first
        List<Long> sortedItems = new ArrayList<>(sampledIds);
        sortedItems.sort(Comparator.comparingInt(it -> itemToTraitsMap.getOrDefault(it, Collections.emptySet()).size()));

        for (Long itemId : sortedItems) {
            Set<Long> candidateTraits = itemToTraitsMap.getOrDefault(itemId, Collections.emptySet());
            augmentItem(itemId, candidateTraits, traitAssigned, traitQuotas, new HashSet<>(), itemToTraitsMap);
        }

        // Fallback: If any item remained unassigned due to edge-case metadata, assign to any trait with available capacity
        Set<Long> assignedItemIds = new HashSet<>();
        for (List<Long> list : traitAssigned.values()) {
            assignedItemIds.addAll(list);
        }

        for (Long itemId : sampledIds) {
            if (!assignedItemIds.contains(itemId)) {
                for (CompetencyTrait t : allTraits) {
                    List<Long> list = traitAssigned.get(t.getId());
                    int quota = traitQuotas.get(t.getId());
                    if (list.size() < quota) {
                        list.add(itemId);
                        assignedItemIds.add(itemId);
                        break;
                    }
                }
            }
        }

        return traitAssigned;
    }

    private boolean augmentItem(Long itemId,
                                Set<Long> candidateTraits,
                                Map<Long, List<Long>> traitAssigned,
                                Map<Long, Integer> traitQuotas,
                                Set<Long> visitedTraits,
                                Map<Long, Set<Long>> itemToTraitsMap) {
        // Prefer candidate traits that are less full
        List<Long> sortedCandidates = new ArrayList<>(candidateTraits);
        sortedCandidates.sort(Comparator.comparingInt(t -> traitAssigned.getOrDefault(t, Collections.emptyList()).size()));

        for (Long traitId : sortedCandidates) {
            if (!traitAssigned.containsKey(traitId) || visitedTraits.contains(traitId)) continue;
            visitedTraits.add(traitId);

            List<Long> currentItems = traitAssigned.get(traitId);
            int quota = traitQuotas.getOrDefault(traitId, 17);

            if (currentItems.size() < quota) {
                currentItems.add(itemId);
                return true;
            } else {
                for (int i = 0; i < currentItems.size(); i++) {
                    Long otherItem = currentItems.get(i);
                    Set<Long> otherCandidates = itemToTraitsMap.getOrDefault(otherItem, Collections.emptySet());
                    if (augmentItem(otherItem, otherCandidates, traitAssigned, traitQuotas, visitedTraits, itemToTraitsMap)) {
                        currentItems.set(i, itemId);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // =========================================================================
    // 2. Derailers Scoring (Distance = |A_i - T_i|, Points = 4 - Distance)
    // =========================================================================
    public static class DerailerScoreResult {
        public double overallPct;
        public List<DerailerCategoryScore> categoryScores = new ArrayList<>();
    }

    private DerailerScoreResult scoreDerailers(BatterySession session) {
        DerailerScoreResult result = new DerailerScoreResult();
        if (session == null) return result;

        List<CandidateResponse> responses = session.getResponses();
        Map<Long, Integer> answerMap = new HashMap<>();
        if (responses != null) {
            for (CandidateResponse cr : responses) {
                if (cr.getItemId() != null && cr.getSelectedLikert() != null) {
                    answerMap.put(cr.getItemId(), cr.getSelectedLikert());
                }
            }
        }

        List<DerailerCategory> allCategories = categoryRepo.findAllByOrderByDisplayOrderAsc();

        // Query derailer items target answers and category mappings
        Map<Long, Integer> targetMap = new HashMap<>();
        Map<Long, Long> itemCategoryMap = new HashMap<>();

        jdbcTemplate.query(
                "SELECT di.id, di.ideal_target, dit.type_id " +
                "FROM derailer_items di " +
                "JOIN derailer_item_types dit ON di.id = dit.item_id",
                rs -> {
                    long itemId = rs.getLong("id");
                    int target = rs.getInt("ideal_target");
                    long catId = rs.getLong("type_id");
                    targetMap.put(itemId, target > 0 ? target : 1);
                    itemCategoryMap.put(itemId, catId);
                }
        );

        List<Long> sampledIds = session.getSampledItemIds();
        if (sampledIds == null || sampledIds.isEmpty()) {
            sampledIds = new ArrayList<>(answerMap.keySet());
        }

        Map<Long, Double> catPointsSum = new HashMap<>();
        Map<Long, Integer> catItemCounts = new HashMap<>();
        double totalPoints = 0.0;

        for (Long itemId : sampledIds) {
            int answer = answerMap.getOrDefault(itemId, 3);
            int target = targetMap.getOrDefault(itemId, 1);
            int distance = Math.abs(answer - target);
            int points = 4 - distance;

            totalPoints += points;

            Long catId = itemCategoryMap.get(itemId);
            if (catId != null) {
                catPointsSum.put(catId, catPointsSum.getOrDefault(catId, 0.0) + points);
                catItemCounts.put(catId, catItemCounts.getOrDefault(catId, 0) + 1);
            }
        }

        int totalItems = sampledIds.isEmpty() ? 60 : sampledIds.size();
        result.overallPct = (totalPoints / (totalItems * 4.0)) * 100.0;

        for (DerailerCategory cat : allCategories) {
            double raw = catPointsSum.getOrDefault(cat.getId(), 0.0);
            int n = catItemCounts.getOrDefault(cat.getId(), 10);
            double pct = (n > 0) ? (raw / (n * 4.0)) * 100.0 : 0.0;
            result.categoryScores.add(new DerailerCategoryScore(null, cat, round2(raw), round2(pct)));
        }

        return result;
    }

    // =========================================================================
    // 3. SJT Scoring (Pairwise Concordance with Expert Ranking, Exclude Ties)
    // =========================================================================
    public double scoreSjt(BatterySession session) {
        if (session == null) return 0.0;

        List<CandidateResponse> responses = session.getResponses();
        Map<Long, List<String>> candidateRankings = new HashMap<>();
        if (responses != null) {
            for (CandidateResponse cr : responses) {
                if (cr.getItemId() != null && cr.getRankingOrder() != null) {
                    candidateRankings.put(cr.getItemId(), cr.getRankingOrder());
                }
            }
        }

        List<Long> sampledIds = session.getSampledItemIds();
        if (sampledIds == null || sampledIds.isEmpty()) {
            sampledIds = new ArrayList<>(candidateRankings.keySet());
        }
        if (sampledIds.isEmpty()) return 0.0;

        // Fetch options with expert scores for sampled scenarios
        String inSql = String.join(",", Collections.nCopies(sampledIds.size(), "?"));
        Map<Long, Map<String, Double>> scenarioExpertScores = new HashMap<>();

        jdbcTemplate.query(
                "SELECT scenario_id, option_key, effectiveness_score FROM sjt_options WHERE scenario_id IN (" + inSql + ")",
                rs -> {
                    long scId = rs.getLong("scenario_id");
                    String optKey = rs.getString("option_key");
                    double score = rs.getDouble("effectiveness_score");
                    scenarioExpertScores.computeIfAbsent(scId, k -> new HashMap<>()).put(optKey, score);
                },
                sampledIds.toArray()
        );

        int totalConcordantPairs = 0;
        int totalValidPairs = 0;

        for (Long scId : sampledIds) {
            Map<String, Double> expertScores = scenarioExpertScores.get(scId);
            List<String> candOrder = candidateRankings.get(scId);

            if (expertScores == null || expertScores.size() < 2) continue;

            List<String> optKeys = new ArrayList<>(expertScores.keySet());
            int numOpts = optKeys.size();

            for (int i = 0; i < numOpts; i++) {
                for (int j = i + 1; j < numOpts; j++) {
                    String optX = optKeys.get(i);
                    String optY = optKeys.get(j);

                    double scoreX = expertScores.get(optX);
                    double scoreY = expertScores.get(optY);

                    // Skip tied pairs (excluded from numerator and denominator)
                    if (Double.compare(scoreX, scoreY) == 0) {
                        continue;
                    }

                    String expertPrefers = (scoreX > scoreY) ? optX : optY;

                    int candIdxX = candOrder != null ? candOrder.indexOf(optX) : -1;
                    int candIdxY = candOrder != null ? candOrder.indexOf(optY) : -1;

                    if (candIdxX == -1 || candIdxY == -1) {
                        // Option not in ranking, cannot evaluate
                        continue;
                    }

                    String candidatePrefers = (candIdxX < candIdxY) ? optX : optY;

                    if (candidatePrefers.equals(expertPrefers)) {
                        totalConcordantPairs++;
                    }
                    totalValidPairs++;
                }
            }
        }

        if (totalValidPairs == 0) return 0.0;
        return ((double) totalConcordantPairs / totalValidPairs) * 100.0;
    }

    // =========================================================================
    // 4. GCAT Scoring (Accuracy per subtest & overall)
    // =========================================================================
    public static class GcatScoreResult {
        public double overallPct;
        public List<GcatSubtestScore> subtestScores = new ArrayList<>();
    }

    public GcatScoreResult scoreGcat(BatterySession session) {
        GcatScoreResult result = new GcatScoreResult();
        if (session == null) return result;

        List<CandidateResponse> responses = session.getResponses();
        Map<Long, String> answerMap = new HashMap<>();
        if (responses != null) {
            for (CandidateResponse cr : responses) {
                if (cr.getItemId() != null && cr.getSelectedOption() != null) {
                    answerMap.put(cr.getItemId(), cr.getSelectedOption());
                }
            }
        }

        List<Long> sampledIds = session.getSampledItemIds();
        if (sampledIds == null || sampledIds.isEmpty()) {
            sampledIds = new ArrayList<>(answerMap.keySet());
        }
        if (sampledIds.isEmpty()) return result;

        String inSql = String.join(",", Collections.nCopies(sampledIds.size(), "?"));
        Map<Long, GcatQuestionMeta> questionMetaMap = new HashMap<>();

        jdbcTemplate.query(
                "SELECT q.id, q.correct_option_key, s.code " +
                "FROM gcat_questions q " +
                "JOIN gcat_subtests s ON q.subtest_id = s.id " +
                "WHERE q.id IN (" + inSql + ")",
                rs -> {
                    long qId = rs.getLong("id");
                    String correctKey = rs.getString("correct_option_key");
                    String subCodeStr = rs.getString("code");
                    GcatSubtestCode subCode = GcatSubtestCode.valueOf(subCodeStr);
                    questionMetaMap.put(qId, new GcatQuestionMeta(correctKey, subCode));
                },
                sampledIds.toArray()
        );

        Map<GcatSubtestCode, Integer> subtestCorrectCount = new HashMap<>();
        Map<GcatSubtestCode, Integer> subtestTotalCount = new HashMap<>();
        int totalCorrect = 0;

        for (Long qId : sampledIds) {
            GcatQuestionMeta meta = questionMetaMap.get(qId);
            if (meta == null) continue;

            String candidateAnswer = answerMap.get(qId);
            boolean isCorrect = candidateAnswer != null && candidateAnswer.trim().equalsIgnoreCase(meta.correctOptionKey.trim());

            if (isCorrect) {
                totalCorrect++;
                subtestCorrectCount.put(meta.subtest, subtestCorrectCount.getOrDefault(meta.subtest, 0) + 1);
            }
            subtestTotalCount.put(meta.subtest, subtestTotalCount.getOrDefault(meta.subtest, 0) + 1);
        }

        int totalQuestions = sampledIds.size();
        result.overallPct = (totalQuestions > 0) ? ((double) totalCorrect / totalQuestions) * 100.0 : 0.0;

        for (GcatSubtestCode sub : GcatSubtestCode.values()) {
            int correct = subtestCorrectCount.getOrDefault(sub, 0);
            int total = subtestTotalCount.getOrDefault(sub, 14);
            double pct = (total > 0) ? ((double) correct / total) * 100.0 : 0.0;
            result.subtestScores.add(new GcatSubtestScore(null, sub, correct, round2(pct)));
        }

        return result;
    }

    private static class GcatQuestionMeta {
        String correctOptionKey;
        GcatSubtestCode subtest;

        GcatQuestionMeta(String correctOptionKey, GcatSubtestCode subtest) {
            this.correctOptionKey = correctOptionKey;
            this.subtest = subtest;
        }
    }

    // =========================================================================
    // 5. Percentile Formula: round[ 100 / (1 + exp(-(Composite - 75.0) / 6.0)) ]
    // =========================================================================
    public static int calculateLogisticPercentile(double compositeScore) {
        double exponent = -(compositeScore - 75.0) / 6.0;
        double percentileDouble = 100.0 / (1.0 + Math.exp(exponent));
        int percentile = (int) Math.round(percentileDouble);
        return Math.max(1, Math.min(99, percentile));
    }

    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
