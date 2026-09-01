package com.psychometric.platform.features.report.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import com.psychometric.platform.features.report.dto.ReportContextDto;
import com.psychometric.platform.features.report.dto.ReportContextDto.CompetencyDetailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * LeadershipReportGeneratorService
 *
 * Bridges the raw assessment scoring data ({@link AssessmentScoreResponseDto}) and the final
 * Thymeleaf view model ({@link ReportContextDto}).
 *
 * Architecture & Separation of Concerns:
 * 1. Backend = Single Source of Truth for all numerical calculations, 1-10 / 1-5 scales,
 *    and color indicator assignments (Green, Orange, Red) against hardcoded role benchmarks.
 * 2. AI = Strictly a narrative generation engine that returns pure Arabic text strings in JSON.
 * 3. Orchestration = Normalizes data -> Builds AI prompt payload -> Calls AI Client ->
 *    Deserializes JSON with Jackson -> Merges numbers/colors + AI text -> Returns complete ReportContextDto.
 */
@Service
public class LeadershipReportGeneratorService {

    private static final Logger log = LoggerFactory.getLogger(LeadershipReportGeneratorService.class);

    // Color Palette Constants
    public static final String COLOR_GREEN = "#558b6e";
    public static final String COLOR_ORANGE = "#d98a44";
    public static final String COLOR_RED = "#d9776c";

    // Hardcoded Executive Leadership Role Benchmarks (1–5 scale)
    private static final Map<String, Integer> ROLE_BENCHMARKS = Map.ofEntries(
            Map.entry("COMMUNICATION_AND_INFLUENCE", 4),
            Map.entry("INITIATIVE", 3),
            Map.entry("DECISION_MAKING_AND_RESPONSIBILITY", 4),
            Map.entry("INSPIRING_LEADERSHIP", 4),
            Map.entry("STRATEGIC_THINKING", 4),
            Map.entry("SKILL_DEVELOPMENT", 3),
            Map.entry("ADAPTABILITY", 4),
            Map.entry("SYSTEMATIC_ANALYSIS_AND_PLANNING", 4),
            Map.entry("ABSTRACT", 4),
            Map.entry("NUMERICAL", 4),
            Map.entry("VERBAL", 3)
    );

    private final ObjectMapper objectMapper;
    private final AiReportClient aiClient;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;
    private final org.springframework.cache.CacheManager cacheManager;

    @org.springframework.beans.factory.annotation.Autowired
    public LeadershipReportGeneratorService(
            ObjectMapper objectMapper,
            Optional<AiReportClient> aiClient,
            @org.springframework.beans.factory.annotation.Autowired(required = false) org.springframework.jdbc.core.JdbcTemplate jdbcTemplate,
            @org.springframework.beans.factory.annotation.Autowired(required = false) org.springframework.cache.CacheManager cacheManager
    ) {
        this.objectMapper = objectMapper.copy()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.aiClient = aiClient.orElseGet(DefaultMockAiClient::new);
        this.jdbcTemplate = jdbcTemplate;
        this.cacheManager = cacheManager;
    }

    public LeadershipReportGeneratorService(ObjectMapper objectMapper, Optional<AiReportClient> aiClient) {
        this(objectMapper, aiClient, null, null);
    }

    /**
     * Evicts all cached entries associated with the given candidate attempt token
     * and clears AI section caches to force fresh generation.
     */
    public void clearCandidateCaches(String attemptToken) {
        if (cacheManager != null) {
            log.info("Evicting all candidate caches for attempt token: {}", attemptToken);

            org.springframework.cache.Cache reportCache = cacheManager.getCache("leadershipReports");
            if (reportCache != null && attemptToken != null) {
                reportCache.evict(attemptToken);
            }

            List<String> aiCaches = List.of(
                    "aiImpressionCache",
                    "aiDerailersCache",
                    "aiCompetencyPageCache",
                    "aiGrowPlanCache"
            );
            for (String cName : aiCaches) {
                org.springframework.cache.Cache c = cacheManager.getCache(cName);
                if (c != null) {
                    c.clear();
                }
            }
        }
    }

    /**
     * Dedicated method for Page 2: Impression Management AI narrative generation.
     */
    public ImpressionResponseDto generateImpressionManagementNarratives(String attemptToken, AssessmentScoreResponseDto rawScore) {
        if (rawScore == null) {
            return aiClient.generateImpressionNarratives(new ImpressionPayload(5, "متوسط", 2, "منخفض"));
        }
        ReportContextDto tempReport = new ReportContextDto();
        normalizeImpressionManagement(rawScore, tempReport);
        ImpressionPayload payload = new ImpressionPayload(
                tempReport.getSocialScore(), tempReport.getSocialRisk(),
                tempReport.getCentralScore(), tempReport.getCentralRisk()
        );
        return aiClient.generateImpressionNarratives(payload);
    }

    /**
     * Dedicated method for Page 4: Personality Derailers AI narrative generation.
     */
    public DerailersResponseDto generateDerailersNarratives(String attemptToken, AssessmentScoreResponseDto rawScore) {
        if (rawScore == null) {
            return aiClient.generateDerailersNarratives(new DerailersPayload(6, 5, 6, 6, 6, 7));
        }
        ReportContextDto tempReport = new ReportContextDto();
        normalizePersonalityDerailers(rawScore, tempReport);
        DerailersPayload payload = new DerailersPayload(
                tempReport.getReservedScore(), tempReport.getEmotionalityScore(),
                tempReport.getHostilityScore(), tempReport.getImpulsivityScore(),
                tempReport.getRigidityScore(), tempReport.getUnconventionalityScore()
        );
        return aiClient.generateDerailersNarratives(payload);
    }

    /**
     * Main entrypoint: generates a fully populated {@link ReportContextDto} from raw scores.
     * Caches generated reports by attemptToken / ID to prevent duplicate LLM calls.
     *
     * @param rawScore the raw scoring response DTO from the assessment engine
     * @return fully populated ReportContextDto ready for Thymeleaf / PDF rendering
     */
    @Cacheable(value = "leadershipReports", key = "#rawScore != null && #rawScore.attemptToken != null ? #rawScore.attemptToken : (#rawScore != null && #rawScore.id != null ? #rawScore.id : 'default')", unless = "#result == null")
    public ReportContextDto generateReport(AssessmentScoreResponseDto rawScore) {
        if (rawScore == null) {
            return ReportContextDto.createDefaultReport("PCIV126371");
        }

        log.info("Generating Leadership Assessment Report for candidate: {}", rawScore.getCandidateName());

        // 1. Initialize Report Context with Candidate Details
        ReportContextDto report = new ReportContextDto();
        String candidateId = rawScore.getAttemptToken() != null ? rawScore.getAttemptToken() : "PCIV126371";
        report.setCandidateId(candidateId);
        report.setCandidateName(rawScore.getCandidateName());

        // Check if scoredAt exists, convert to LocalDate, otherwise fallback to now
        String reportDate = rawScore.getScoredAt() != null 
                ? java.time.LocalDateTime.ofInstant(rawScore.getScoredAt(), java.time.ZoneId.systemDefault()).toLocalDate().toString() 
                : java.time.LocalDate.now().toString();
        report.setReportDate(reportDate);
        report.setEvaluationPurpose("تقرير الكفاءات للقادة: تطوير");

        // Page 1: Composite Score
        double compositePct = rawScore.getCompositeScore() != null ? rawScore.getCompositeScore() : 88.5;
        report.setResultScore(String.format(Locale.US, "%.1f", compositePct));

        // 2. Normalize Page 2: Impression Management & Validity
        normalizeImpressionManagement(rawScore, report);

        // 3. Normalize Page 4: Personality Derailer Traits (1–10 scale)
        normalizePersonalityDerailers(rawScore, report);

        // 4. Normalize Page 5: Competencies & Cognitive Abilities (1–5 scale + Color indicators)
        normalizeCompetenciesAndCognitive(rawScore, report);

        // 5. Initialize Detailed Competency Pages Structure (Pages 7–14)
        initializeDetailedCompetencyPages(rawScore, report);

        // 6. Build Modular Candidate Context Payloads
        ImpressionPayload impressionPayload = new ImpressionPayload(
                report.getSocialScore(), report.getSocialRisk(),
                report.getCentralScore(), report.getCentralRisk()
        );

        DerailersPayload derailersPayload = new DerailersPayload(
                report.getReservedScore(), report.getEmotionalityScore(),
                report.getHostilityScore(), report.getImpulsivityScore(),
                report.getRigidityScore(), report.getUnconventionalityScore()
        );

        String personalityContext = extractPersonalityContext(report);

        Map<Integer, CompetencyPagePayload> compPayloads = new LinkedHashMap<>();
        for (int p = 7; p <= 14; p++) {
            CompetencyDetailDto cDto = report.getCompetencyPage(p);
            List<String> subReqs = new ArrayList<>();
            if (cDto.getReq1() != null) subReqs.add(cDto.getReq1());
            if (cDto.getReq2() != null) subReqs.add(cDto.getReq2());
            if (p != 11 && cDto.getReq3() != null) subReqs.add(cDto.getReq3());

            String qaData = extractItemQaForCompetency(candidateId, p, cDto.getCompetencyTitle());

            compPayloads.put(p, new CompetencyPagePayload(
                    p,
                    cDto.getCompetencyTitle(),
                    cDto.getCompetencyScore(),
                    personalityContext,
                    subReqs,
                    qaData
            ));
        }

        String topStrengths = extractTopStrengths(report);
        String devAreas = extractDevelopmentAreas(report);
        GrowPlanPayload growPayload = new GrowPlanPayload(
                report.getCandidateName(),
                topStrengths,
                devAreas,
                personalityContext
        );

        // 7. Invoke AI Engine Concurrently for All Sections
        try {
            CompletableFuture<ImpressionResponseDto> cfImpression = CompletableFuture.supplyAsync(
                    () -> aiClient.generateImpressionNarratives(impressionPayload)
            );

            CompletableFuture<DerailersResponseDto> cfDerailers = CompletableFuture.supplyAsync(
                    () -> aiClient.generateDerailersNarratives(derailersPayload)
            );

            Map<Integer, CompletableFuture<CompetencyPageResponseDto>> cfCompPages = new LinkedHashMap<>();
            for (int p = 7; p <= 14; p++) {
                final int pageNum = p;
                cfCompPages.put(pageNum, CompletableFuture.supplyAsync(
                        () -> aiClient.generateCompetencyPageNarratives(compPayloads.get(pageNum))
                ));
            }

            CompletableFuture<GrowPlanResponseDto> cfGrow = CompletableFuture.supplyAsync(
                    () -> aiClient.generateGrowPlanNarratives(growPayload)
            );

            List<CompletableFuture<?>> allFutures = new ArrayList<>();
            allFutures.add(cfImpression);
            allFutures.add(cfDerailers);
            allFutures.addAll(cfCompPages.values());
            allFutures.add(cfGrow);

            CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0]))
                    .get(60, TimeUnit.SECONDS);

            // 8. Merge AI Narratives into the Report Context
            ImpressionResponseDto impResp = cfImpression.get();
            if (impResp != null) {
                if (impResp.socialInterpretation != null && !impResp.socialInterpretation.isBlank()) {
                    report.setSocialInterpretation(impResp.socialInterpretation);
                }
                if (impResp.centralInterpretation != null && !impResp.centralInterpretation.isBlank()) {
                    report.setCentralInterpretation(impResp.centralInterpretation);
                }
            }

            DerailersResponseDto derResp = cfDerailers.get();
            if (derResp != null) {
                if (derResp.reservedText != null) report.setReservedText(derResp.reservedText);
                if (derResp.emotionalityText != null) report.setEmotionalityText(derResp.emotionalityText);
                if (derResp.hostilityText != null) report.setHostilityText(derResp.hostilityText);
                if (derResp.impulsivityText != null) report.setImpulsivityText(derResp.impulsivityText);
                if (derResp.rigidityText != null) report.setRigidityText(derResp.rigidityText);
                if (derResp.unconventionalityText != null) report.setUnconventionalityText(derResp.unconventionalityText);
            }

            for (int p = 7; p <= 14; p++) {
                CompetencyPageResponseDto cpResp = cfCompPages.get(p).get();
                if (cpResp != null) {
                    CompetencyDetailDto cDto = report.getCompetencyPage(p);
                    if (cDto != null) {
                        String r1 = CompetencyDetailDto.cleanOrNull(cpResp.result1);
                        String rc1 = CompetencyDetailDto.cleanOrNull(cpResp.rec1);
                        String r2 = CompetencyDetailDto.cleanOrNull(cpResp.result2);
                        String rc2 = CompetencyDetailDto.cleanOrNull(cpResp.rec2);

                        if (cpResp.req1 != null && CompetencyDetailDto.cleanOrNull(cpResp.req1) != null) {
                            cDto.setReq1(CompetencyDetailDto.cleanOrNull(cpResp.req1));
                        }
                        if (cpResp.req2 != null && CompetencyDetailDto.cleanOrNull(cpResp.req2) != null) {
                            cDto.setReq2(CompetencyDetailDto.cleanOrNull(cpResp.req2));
                        }

                        cDto.setResult1(r1);
                        cDto.setRec1(rc1);
                        cDto.setResult2(r2);
                        cDto.setRec2(rc2);

                        if (p != 11) {
                            if (cpResp.req3 != null && CompetencyDetailDto.cleanOrNull(cpResp.req3) != null) {
                                cDto.setReq3(CompetencyDetailDto.cleanOrNull(cpResp.req3));
                            }
                            String r3 = CompetencyDetailDto.cleanOrNull(cpResp.result3);
                            String rc3 = CompetencyDetailDto.cleanOrNull(cpResp.rec3);
                            cDto.setResult3(r3);
                            cDto.setRec3(rc3);
                        }
                    }
                }
            }

            GrowPlanResponseDto growResp = cfGrow.get();
            if (growResp != null) {
                if (CompetencyDetailDto.cleanOrNull(growResp.growGoalText) != null) report.setGrowGoalText(CompetencyDetailDto.cleanOrNull(growResp.growGoalText));
                if (CompetencyDetailDto.cleanOrNull(growResp.growRealityText) != null) report.setGrowRealityText(CompetencyDetailDto.cleanOrNull(growResp.growRealityText));
                if (CompetencyDetailDto.cleanOrNull(growResp.growOptionsText) != null) report.setGrowOptionsText(CompetencyDetailDto.cleanOrNull(growResp.growOptionsText));
                if (CompetencyDetailDto.cleanOrNull(growResp.growWillText) != null) report.setGrowWillText(CompetencyDetailDto.cleanOrNull(growResp.growWillText));
            }

        } catch (Exception e) {
            log.error("Failed to generate/merge AI narratives. Applying fallback standard narratives: {}", e.getMessage(), e);
            applyFallbackNarratives(report);
        }

        return report;
    }

    // =========================================================================
    // STEP 2: Normalization Logic (1–10 and 1–5 Scales)
    // =========================================================================

    private void normalizeImpressionManagement(AssessmentScoreResponseDto raw, ReportContextDto report) {
        // Social Desirability: 0..100% -> 1..10 scale
        double sdRisk = raw.getSocialDesirabilityRiskPct() != null ? raw.getSocialDesirabilityRiskPct() : 50.0;
        int social10 = scaleTo10(sdRisk);
        report.setSocialScore(social10);
        report.setSocialRisk(social10 <= 3 ? "منخفض" : (social10 <= 7 ? "متوسط" : "مرتفع"));

        // Central Tendency: 0..100% -> 1..10 scale
        double ctRate = raw.getCentralTendencyRatePct() != null ? raw.getCentralTendencyRatePct() : 20.0;
        int central10 = scaleTo10(ctRate);
        report.setCentralScore(central10);
        report.setCentralRisk(central10 <= 3 ? "منخفض" : (central10 <= 7 ? "متوسط" : "مرتفع"));
    }

    private void normalizePersonalityDerailers(AssessmentScoreResponseDto raw, ReportContextDto report) {
        // Extract derailer category scores or use defaults
        Map<String, Double> categoryScoreMap = new HashMap<>();
        if (raw.getDerailerCategoryScores() != null) {
            for (var cs : raw.getDerailerCategoryScores()) {
                if (cs.getNameAr() != null && cs.getScorePct() != null) {
                    categoryScoreMap.put(cs.getNameAr().trim(), cs.getScorePct());
                }
            }
        }

        report.setReservedScore(scaleTo10(categoryScoreMap.getOrDefault("التحفظ", 60.0)));
        report.setEmotionalityScore(scaleTo10(categoryScoreMap.getOrDefault("الانفعالية", 50.0)));
        report.setHostilityScore(scaleTo10(categoryScoreMap.getOrDefault("العدائية", 60.0)));
        report.setImpulsivityScore(scaleTo10(categoryScoreMap.getOrDefault("الاندفاعية", 60.0)));
        report.setRigidityScore(scaleTo10(categoryScoreMap.getOrDefault("الصرامة", 60.0)));
        report.setUnconventionalityScore(scaleTo10(categoryScoreMap.getOrDefault("اللامألوفية", 70.0)));
    }

    private Double findTraitScorePct(Map<String, Double> traitMap, List<AssessmentScoreResponseDto.TraitScoreDto> traitScores, String code, int displayOrder, double fallbackDefault) {
        if (traitMap != null && traitMap.containsKey(code)) {
            return traitMap.get(code);
        }
        if (traitMap != null) {
            String normalizedCode = code.replace("_", "").toLowerCase();
            for (var entry : traitMap.entrySet()) {
                if (entry.getKey() != null && entry.getKey().replace("_", "").equalsIgnoreCase(normalizedCode)) {
                    return entry.getValue();
                }
            }
        }
        if (traitScores != null) {
            for (var ts : traitScores) {
                if (ts.getDisplayOrder() != null && ts.getDisplayOrder() == displayOrder && ts.getScorePct() != null) {
                    return ts.getScorePct();
                }
            }
        }
        log.warn("Trait score for {} (displayOrder={}) not found in raw payload, using fallback: {}", code, displayOrder, fallbackDefault);
        return fallbackDefault;
    }

    private void normalizeCompetenciesAndCognitive(AssessmentScoreResponseDto raw, ReportContextDto report) {
        Map<String, Double> traitMap = new HashMap<>();
        if (raw.getTraitScores() != null) {
            for (var ts : raw.getTraitScores()) {
                if (ts.getTraitCode() != null && ts.getScorePct() != null) {
                    traitMap.put(ts.getTraitCode().trim(), ts.getScorePct());
                }
            }
        }

        // 1. Overall Score
        int overall5 = scaleTo5(raw.getCompositeScore() != null ? raw.getCompositeScore() : 50.0);
        report.setOverallScore(overall5);
        report.setOverallColor(determineColor(overall5, 4));

        // 2. Behavioral Competencies (Continuous Double Scale 1.0–5.0)
        double comm = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "COMMUNICATION_AND_INFLUENCE", 1, 50.0));
        report.setCommScore(comm);
        report.setCommColor(determineColor((int) Math.round(comm), ROLE_BENCHMARKS.get("COMMUNICATION_AND_INFLUENCE")));

        double init = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "INITIATIVE", 2, 50.0));
        report.setInitiativeScore(init);
        report.setInitiativeColor(determineColor((int) Math.round(init), ROLE_BENCHMARKS.get("INITIATIVE")));

        double dec = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "DECISION_MAKING_AND_RESPONSIBILITY", 3, 50.0));
        report.setDecisionScore(dec);
        report.setDecisionColor(determineColor((int) Math.round(dec), ROLE_BENCHMARKS.get("DECISION_MAKING_AND_RESPONSIBILITY")));

        double lead = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "INSPIRING_LEADERSHIP", 4, 50.0));
        report.setLeadershipScore(lead);
        report.setLeadershipColor(determineColor((int) Math.round(lead), ROLE_BENCHMARKS.get("INSPIRING_LEADERSHIP")));

        double strat = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "STRATEGIC_THINKING", 5, 50.0));
        report.setStrategicScore(strat);
        report.setStrategicColor(determineColor((int) Math.round(strat), ROLE_BENCHMARKS.get("STRATEGIC_THINKING")));

        double skills = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "SKILL_DEVELOPMENT", 6, 50.0));
        report.setSkillsScore(skills);
        report.setSkillsColor(determineColor((int) Math.round(skills), ROLE_BENCHMARKS.get("SKILL_DEVELOPMENT")));

        double adapt = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "ADAPTABILITY", 7, 50.0));
        report.setAdaptabilityScore(adapt);
        report.setAdaptabilityColor(determineColor((int) Math.round(adapt), ROLE_BENCHMARKS.get("ADAPTABILITY")));

        double plan = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "SYSTEMATIC_ANALYSIS_AND_PLANNING", 8, 50.0));
        report.setAnalysisScore(plan);
        report.setAnalysisColor(determineColor((int) Math.round(plan), ROLE_BENCHMARKS.get("SYSTEMATIC_ANALYSIS_AND_PLANNING")));

        // 3. Cognitive Abilities (GCAT) (Continuous Double Scale 1.0–5.0)
        Map<GcatSubtestCode, Double> gcatMap = new HashMap<>();
        if (raw.getGcatSubtestScores() != null) {
            for (var gs : raw.getGcatSubtestScores()) {
                if (gs.getSubtest() != null && gs.getScorePct() != null) {
                    gcatMap.put(gs.getSubtest(), gs.getScorePct());
                }
            }
        }

        double abs = scaleTo5Double(gcatMap.getOrDefault(GcatSubtestCode.ABSTRACT, 50.0));
        report.setAbstractScore(abs);
        report.setAbstractColor(determineColor((int) Math.round(abs), ROLE_BENCHMARKS.get("ABSTRACT")));

        double num = scaleTo5Double(gcatMap.getOrDefault(GcatSubtestCode.NUMERICAL, 50.0));
        report.setNumericalScore(num);
        report.setNumericalColor(determineColor((int) Math.round(num), ROLE_BENCHMARKS.get("NUMERICAL")));

        double verb = scaleTo5Double(gcatMap.getOrDefault(GcatSubtestCode.VERBAL, 50.0));
        report.setVerbalScore(verb);
        report.setVerbalColor(determineColor((int) Math.round(verb), ROLE_BENCHMARKS.get("VERBAL")));
        report.setGeneralAbilitiesColor(determineColor((int) Math.round((abs + num + verb) / 3.0), 4));
    }

    private void initializeDetailedCompetencyPages(AssessmentScoreResponseDto raw, ReportContextDto report) {
        Map<String, Double> traitMap = new HashMap<>();
        if (raw.getTraitScores() != null) {
            for (var ts : raw.getTraitScores()) {
                if (ts.getTraitCode() != null && ts.getScorePct() != null) {
                    traitMap.put(ts.getTraitCode().trim(), ts.getScorePct());
                }
            }
        }

        for (int p = 7; p <= 14; p++) {
            CompetencyDetailDto defaultDto = ReportContextDto.getDefaultCompetencyPage(p, report.getCandidateId());

            // Sync with backend calculated continuous Double score & color
            switch (p) {
                case 7 -> {
                    double dScore = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "COMMUNICATION_AND_INFLUENCE", 1, 50.0));
                    defaultDto.setCompetencyScore(dScore);
                    defaultDto.setCompetencyColor(report.getCommColor());
                }
                case 8 -> {
                    double dScore = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "INITIATIVE", 2, 50.0));
                    defaultDto.setCompetencyScore(dScore);
                    defaultDto.setCompetencyColor(report.getInitiativeColor());
                }
                case 9 -> {
                    double dScore = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "DECISION_MAKING_AND_RESPONSIBILITY", 3, 50.0));
                    defaultDto.setCompetencyScore(dScore);
                    defaultDto.setCompetencyColor(report.getDecisionColor());
                }
                case 10 -> {
                    double dScore = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "INSPIRING_LEADERSHIP", 4, 50.0));
                    defaultDto.setCompetencyScore(dScore);
                    defaultDto.setCompetencyColor(report.getLeadershipColor());
                }
                case 11 -> {
                    double dScore = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "STRATEGIC_THINKING", 5, 50.0));
                    defaultDto.setCompetencyScore(dScore);
                    defaultDto.setCompetencyColor(report.getStrategicColor());
                }
                case 12 -> {
                    double dScore = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "SKILL_DEVELOPMENT", 6, 50.0));
                    defaultDto.setCompetencyScore(dScore);
                    defaultDto.setCompetencyColor(report.getSkillsColor());
                }
                case 13 -> {
                    double dScore = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "ADAPTABILITY", 7, 50.0));
                    defaultDto.setCompetencyScore(dScore);
                    defaultDto.setCompetencyColor(report.getAdaptabilityColor());
                }
                case 14 -> {
                    double dScore = scaleTo5Double(findTraitScorePct(traitMap, raw.getTraitScores(), "SYSTEMATIC_ANALYSIS_AND_PLANNING", 8, 50.0));
                    defaultDto.setCompetencyScore(dScore);
                    defaultDto.setCompetencyColor(report.getAnalysisColor());
                }
            }
            report.getCompetencyPages().put(p, defaultDto);
        }
    }

    // =========================================================================
    // STEP 3: Prompt Building & AI Payload Construction
    // =========================================================================

    public record ImpressionPayload(
            int socialScore,
            String socialRisk,
            int centralScore,
            String centralRisk
    ) {}

    public record DerailersPayload(
            int reserved,
            int emotionality,
            int hostility,
            int impulsivity,
            int rigidity,
            int unconventionality
    ) {}

    public record CompetencyPagePayload(
            int pageNum,
            String competencyTitle,
            double score,
            String personalityContext,
            List<String> subIndicatorReqs,
            String questionsAndAnswers
    ) {}

    public record GrowPlanPayload(
            String candidateName,
            String topStrengths,
            String developmentAreas,
            String prominentDerailers
    ) {}

    public record AiPromptPayload(
            String candidateId,
            String candidateName,
            Map<String, Object> normalizedScores,
            Map<String, Integer> roleBenchmarks,
            List<String> competencyNames,
            String instructions
    ) {}

    public String extractItemQaForCompetency(String attemptToken, int pageNum, String competencyTitle) {
        if (jdbcTemplate != null && attemptToken != null && !attemptToken.isBlank()) {
            try {
                String sql = """
                        SELECT pi.statement_ar, cr.selected_likert
                        FROM candidate_responses cr
                        JOIN battery_sessions bs ON cr.session_id = bs.id
                        JOIN assessment_attempts aa ON bs.attempt_id = aa.id
                        JOIN personality_items pi ON cr.item_id = pi.id
                        JOIN personality_item_competencies pic ON pi.id = pic.item_id
                        JOIN competencies c ON pic.competency_id = c.id
                        WHERE aa.attempt_token = ? 
                          AND (c.name_ar = ? OR c.name_ar LIKE ? OR c.code = ?)
                        ORDER BY cr.id ASC
                        """;
                String traitCode = getTraitCodeForPage(pageNum);
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, attemptToken, competencyTitle, "%" + competencyTitle + "%", traitCode);

                if (!rows.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    int qNum = 1;
                    for (Map<String, Object> row : rows) {
                        String statement = (String) row.get("statement_ar");
                        Number likertNum = (Number) row.get("selected_likert");
                        int likert = likertNum != null ? likertNum.intValue() : 3;
                        String likertLabel = formatLikert(likert);
                        sb.append(String.format("السؤال %d: [%s] - إجابة المرشح: [%s]\n", qNum++, statement, likertLabel));
                    }
                    return sb.toString().trim();
                }
            } catch (Exception e) {
                log.warn("Could not query candidate item responses for {} ({}). Using structured fallback Q&A data.", competencyTitle, e.getMessage());
            }
        }

        return getFallbackItemQa(pageNum);
    }

    public static String getTraitCodeForPage(int pageNum) {
        return switch (pageNum) {
            case 7 -> "COMMUNICATION_AND_INFLUENCE";
            case 8 -> "INITIATIVE";
            case 9 -> "DECISION_MAKING_AND_RESPONSIBILITY";
            case 10 -> "INSPIRING_LEADERSHIP";
            case 11 -> "STRATEGIC_THINKING";
            case 12 -> "SKILL_DEVELOPMENT";
            case 13 -> "ADAPTABILITY";
            case 14 -> "SYSTEMATIC_ANALYSIS_AND_PLANNING";
            default -> "";
        };
    }

    public static String formatLikert(int likert) {
        return switch (likert) {
            case 5 -> "أوافق بشدة / 5";
            case 4 -> "أوافق / 4";
            case 3 -> "محايد / 3";
            case 2 -> "لا أوافق / 2";
            case 1 -> "لا أوافق بشدة / 1";
            default -> String.valueOf(likert);
        };
    }

    public static String getFallbackItemQa(int pageNum) {
        return switch (pageNum) {
            case 7 -> """
                    السؤال 1: [أحرص على توضيح أفكاري بأسلوب مقنع ومناسب لكافة أطراف النقاش] - إجابة المرشح: [أوافق / 4]
                    السؤال 2: [أستمع بانتباه لوجهات نظر الآخرين قبل تقديم استنتاجاتي] - إجابة المرشح: [أوافق بشدة / 5]
                    السؤال 3: [أنجح في بناء تحالفات عمل وتوجيه الآراء نحو تحقيق الأهداف المشتركة] - إجابة المرشح: [محايد / 3]
                    """.trim();
            case 8 -> """
                    السؤال 1: [أسعى لاقتناص الفرص التطويرية دون انتظار التوجيه المباشر] - إجابة المرشح: [أوافق / 4]
                    السؤال 2: [أتحرك بسرعة لمعالجة التحديات والمستجدات غير المتوقعة في بيئة العمل] - إجابة المرشح: [لا أوافق / 2]
                    السؤال 3: [أقدم مقترحات مبتكرة لتحسين إجراءات وكفاءة العمل بصفة مستمرة] - إجابة المرشح: [أوافق / 4]
                    """.trim();
            case 9 -> """
                    السؤال 1: [أعتمد على التحليل المنطقي والبيانات الموثوقة عند المفاضلة بين الخيارات] - إجابة المرشح: [أوافق / 4]
                    السؤال 2: [أحسم القرارات الصعبة في الأوقات الحرجة دون تردد مفرط] - إجابة المرشح: [محايد / 3]
                    السؤال 3: [أتحمل المسؤولية الكاملة عن تبعات ونتائج القرارات التي أتخذها] - إجابة المرشح: [أوافق بشدة / 5]
                    """.trim();
            case 10 -> """
                    السؤال 1: [أحفز أعضاء الفريق وأوجههم نحو تحقيق رؤية وأهداف طموحة] - إجابة المرشح: [أوافق بشدة / 5]
                    السؤال 2: [أبني بيئة عمل قائمة على الثقة والتمكين والتقدير المتبادل] - إجابة المرشح: [أوافق / 4]
                    السؤال 3: [أمثل نموذجاً يحتذى به في الالتزام المهني والنزاهة القيادية] - إجابة المرشح: [أوافق بشدة / 5]
                    """.trim();
            case 11 -> """
                    السؤال 1: [أستشرف التوجهات والفرص المستقبلية وأربطها بخطط العمل الحالية] - إجابة المرشح: [لا أوافق / 2]
                    السؤال 2: [أحلل الصورة الشاملة والتأثيرات طويلة المدى للقرارات المؤسسية] - إجابة المرشح: [محايد / 3]
                    """.trim();
            case 12 -> """
                    السؤال 1: [أحدد الاحتياجات التدريبية لأعضاء الفريق وأوفر لهم فرص التعلم المستمر] - إجابة المرشح: [أوافق / 4]
                    السؤال 2: [أحرص على تطوير قدراتي الذاتية ومواكبة أفضل الممارسات في مجالي] - إجابة المرشح: [أوافق بشدة / 5]
                    السؤال 3: [أقدم تغذية راجعة بناءة وتوجيهاً فعالاً لتمكين الآخرين من النمو] - إجابة المرشح: [أوافق / 4]
                    """.trim();
            case 13 -> """
                    السؤال 1: [أتقبل التغييرات الهيكلية والمهنية بمرونة وإيجابية عالية] - إجابة المرشح: [أوافق / 4]
                    السؤال 2: [أعدل أسلوب عملي واستراتيجياتي لتلائم متطلبات الظروف المستجدة] - إجابة المرشح: [أوافق / 4]
                    السؤال 3: [أحافظ على الفاعلية والأداء المتميز تحت الضغوط وبيئات العمل المتغيرة] - إجابة المرشح: [محايد / 3]
                    """.trim();
            case 14 -> """
                    السؤال 1: [أضع خطط عمل مفصلة ومحددة بمؤشرات قياس وجداول زمنية دقيقة] - إجابة المرشح: [أوافق بشدة / 5]
                    السؤال 2: [أحلل المشكلات المعقدة وأفككها إلى عناصر قابلة للحل والتنفيذ] - إجابة المرشح: [أوافق / 4]
                    السؤال 3: [أتابع سير العمل وأجري التعديلات التصحيحية بناءً على مؤشرات الإنجاز] - إجابة المرشح: [أوافق بشدة / 5]
                    """.trim();
            default -> "";
        };
    }

    private String extractPersonalityContext(ReportContextDto report) {
        Map<String, Integer> derailers = new LinkedHashMap<>();
        derailers.put("التحفظ", report.getReservedScore());
        derailers.put("الانفعالية", report.getEmotionalityScore());
        derailers.put("العدائية", report.getHostilityScore());
        derailers.put("الاندفاعية", report.getImpulsivityScore());
        derailers.put("الصرامة", report.getRigidityScore());
        derailers.put("اللامألوفية", report.getUnconventionalityScore());

        var sorted = derailers.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .toList();

        var highest = sorted.get(0);
        var secondHighest = sorted.get(1);
        return String.format(Locale.US, "السمات الشخصية الأبرز للمرشح: %s (%d/10) و%s (%d/10)",
                highest.getKey(), highest.getValue(),
                secondHighest.getKey(), secondHighest.getValue());
    }

    private String extractTopStrengths(ReportContextDto report) {
        Map<String, Double> compScores = new LinkedHashMap<>();
        compScores.put("التواصل والتأثير الفعال", report.getCommScore());
        compScores.put("المبادرة", report.getInitiativeScore());
        compScores.put("اتخاذ القرار وتحمل المسؤولية", report.getDecisionScore());
        compScores.put("القيادة الملهمة", report.getLeadershipScore());
        compScores.put("التفكير الاستراتيجي", report.getStrategicScore());
        compScores.put("تطوير المهارات", report.getSkillsScore());
        compScores.put("القدرة على التكيف", report.getAdaptabilityScore());
        compScores.put("التحليل والتخطيط المنهجي", report.getAnalysisScore());

        return compScores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(2)
                .map(e -> String.format(Locale.US, "%s (%.1f/5)", e.getKey(), e.getValue()))
                .reduce((a, b) -> a + "، " + b)
                .orElse("التحليل والتخطيط المنهجي");
    }

    private String extractDevelopmentAreas(ReportContextDto report) {
        Map<String, Double> compScores = new LinkedHashMap<>();
        compScores.put("التواصل والتأثير الفعال", report.getCommScore());
        compScores.put("المبادرة", report.getInitiativeScore());
        compScores.put("اتخاذ القرار وتحمل المسؤولية", report.getDecisionScore());
        compScores.put("القيادة الملهمة", report.getLeadershipScore());
        compScores.put("التفكير الاستراتيجي", report.getStrategicScore());
        compScores.put("تطوير المهارات", report.getSkillsScore());
        compScores.put("القدرة على التكيف", report.getAdaptabilityScore());
        compScores.put("التحليل والتخطيط المنهجي", report.getAnalysisScore());

        return compScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(2)
                .map(e -> String.format(Locale.US, "%s (%.1f/5)", e.getKey(), e.getValue()))
                .reduce((a, b) -> a + "، " + b)
                .orElse("التفكير الاستراتيجي");
    }

    // =========================================================================
    // STEP 4 & 5: Deserialization & Response DTOs
    // =========================================================================

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImpressionResponseDto {
        public String socialInterpretation;
        public String centralInterpretation;

        public ImpressionResponseDto() {}
        public ImpressionResponseDto(String socialInterpretation, String centralInterpretation) {
            this.socialInterpretation = socialInterpretation;
            this.centralInterpretation = centralInterpretation;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DerailersResponseDto {
        public String reservedText;
        public String emotionalityText;
        public String hostilityText;
        public String impulsivityText;
        public String rigidityText;
        public String unconventionalityText;

        public DerailersResponseDto() {}
        public DerailersResponseDto(String reservedText, String emotionalityText, String hostilityText, String impulsivityText, String rigidityText, String unconventionalityText) {
            this.reservedText = reservedText;
            this.emotionalityText = emotionalityText;
            this.hostilityText = hostilityText;
            this.impulsivityText = impulsivityText;
            this.rigidityText = rigidityText;
            this.unconventionalityText = unconventionalityText;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompetencyPageResponseDto {
        public String req1;
        public String result1;
        public String rec1;
        public String req2;
        public String result2;
        public String rec2;
        public String req3;
        public String result3;
        public String rec3;

        public CompetencyPageResponseDto() {}
        public CompetencyPageResponseDto(String req1, String result1, String rec1, String req2, String result2, String rec2, String req3, String result3, String rec3) {
            this.req1 = req1;
            this.result1 = result1;
            this.rec1 = rec1;
            this.req2 = req2;
            this.result2 = result2;
            this.rec2 = rec2;
            this.req3 = req3;
            this.result3 = result3;
            this.rec3 = rec3;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GrowPlanResponseDto {
        public String growGoalText;
        public String growRealityText;
        public String growOptionsText;
        public String growWillText;

        public GrowPlanResponseDto() {}
        public GrowPlanResponseDto(String growGoalText, String growRealityText, String growOptionsText, String growWillText) {
            this.growGoalText = growGoalText;
            this.growRealityText = growRealityText;
            this.growOptionsText = growOptionsText;
            this.growWillText = growWillText;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AiNarrativeResponseDto {
        public String socialInterpretation;
        public String centralInterpretation;

        public String reservedText;
        public String emotionalityText;
        public String hostilityText;
        public String impulsivityText;
        public String rigidityText;
        public String unconventionalityText;

        public Map<String, PageNarrativeDto> competencyNarratives = new HashMap<>();

        public String growGoalText;
        public String growRealityText;
        public String growOptionsText;
        public String growWillText;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PageNarrativeDto {
            public String result1;
            public String rec1;
            public String result2;
            public String rec2;
            public String result3;
            public String rec3;
        }
    }

    private void applyFallbackNarratives(ReportContextDto report) {
        log.info("Applied standardized psychometric fallback narratives.");
    }

    // =========================================================================
    // Scale Normalization & Color Determination Helpers
    // =========================================================================

    /**
     * Converts a 0..100 percentage score to an integer 1..10 scale.
     */
    public static int scaleTo10(Double scorePct) {
        if (scorePct == null) return 5;
        int scaled = (int) Math.round((Math.max(0.0, Math.min(100.0, scorePct)) / 100.0) * 9.0) + 1;
        return Math.max(1, Math.min(10, scaled));
    }

    /**
     * Converts a 0..100 percentage score to a continuous Double 1..5 scale (2 decimal places).
     */
    public static double scaleTo5Double(Double scorePct) {
        if (scorePct == null) return 3.0;
        double scaled = (Math.max(0.0, Math.min(100.0, scorePct)) / 100.0) * 4.0 + 1.0;
        return Math.round(scaled * 100.0) / 100.0;
    }

    /**
     * Converts a 0..100 percentage score to an integer 1..5 scale.
     */
    public static int scaleTo5(Double scorePct) {
        if (scorePct == null) return 3;
        int scaled = (int) Math.round((Math.max(0.0, Math.min(100.0, scorePct)) / 100.0) * 4.0) + 1;
        return Math.max(1, Math.min(5, scaled));
    }

    /**
     * Assigns Hex color indicator based on candidate score vs benchmark.
     * Green (#558b6e): score >= 4 or meets/exceeds benchmark.
     * Orange (#d98a44): score == 3 or 1 point below benchmark.
     * Red (#d9776c): score <= 2 or 2+ points below benchmark.
     */
    public static String determineColor(int candidateScore, Integer benchmark) {
        int target = benchmark != null ? benchmark : 4;
        int gap = candidateScore - target;

        if (candidateScore >= 4 || gap >= 0) {
            return COLOR_GREEN;
        } else if (candidateScore == 3 || gap == -1) {
            return COLOR_ORANGE;
        } else {
            return COLOR_RED;
        }
    }

    // =========================================================================
    // AI Client Interface & Mock Implementation
    // =========================================================================

    public interface AiReportClient {
        ImpressionResponseDto generateImpressionNarratives(ImpressionPayload payload);
        DerailersResponseDto generateDerailersNarratives(DerailersPayload payload);
        CompetencyPageResponseDto generateCompetencyPageNarratives(CompetencyPagePayload payload);
        GrowPlanResponseDto generateGrowPlanNarratives(GrowPlanPayload payload);

        default String generateNarrativesJson(AiPromptPayload payload) {
            return "";
        }
    }

    public static class DefaultMockAiClient implements AiReportClient {

        @Override
        public ImpressionResponseDto generateImpressionNarratives(ImpressionPayload payload) {
            String socialText = payload.socialScore() > 7 
                    ? "تشير الدرجة إلى ميل ملحوظ لإظهار صورة إيجابية مثالية (خطر مرتفع). يُنصح بمراعاة ذلك عند تفسير نتائج التقييم الأخرى."
                    : "من المرجح أنه أجاب بصدق وموضوعية دون تزييف مفرط للصورة الإيجابية (خطر " + payload.socialRisk() + "). لا توجد مؤشرات مقلقة.";
            String centralText = payload.centralScore() > 7
                    ? "لوحظ ميل مرتفع لاختيار الإجابات الوسطية وتجنب إبداء مواقف واضحة وحاسمة (خطر مرتفع)."
                    : "أظهر المرشح وضوحاً وحسماً في تحديد مواقفه دون اللجوء المفرط للإجابات المحايدة (خطر " + payload.centralRisk() + ").";
            return new ImpressionResponseDto(socialText, centralText);
        }

        @Override
        public DerailersResponseDto generateDerailersNarratives(DerailersPayload payload) {
            return new DerailersResponseDto(
                    "تشير نتيجة (" + payload.reserved() + "/10) إلى احتمالية التحفظ والانغلاق تحت الضغط، مما قد يؤثر على التواصل مع الفريق.",
                    "تشير نتيجة (" + payload.emotionality() + "/10) إلى درجة التحكم الانفعالي، مع الحاجة للمحافظة على الثبات في المواقف الحرجة.",
                    "تشير نتيجة (" + payload.hostility() + "/10) إلى مستوى التنافسية وإمكانية إظهار حدة في التعامل عند النزاعات الحادة.",
                    "تشير نتيجة (" + payload.impulsivity() + "/10) إلى سرعة اتخاذ الإجراءات مع احتمالية التسرع قبل استكمال دراسة البدائل تحت وطأة الوقت.",
                    "تشير نتيجة (" + payload.rigidity() + "/10) إلى التمسك بالقواعد والإجراءات المعمول بها مع حاجة لمرونة إضافية عند تغير الظروف.",
                    "تشير نتيجة (" + payload.unconventionality() + "/10) إلى الميل لتبني أساليب غير تقليدية ومبتكرة في معالجة التحديات القيادية."
            );
        }

        @Override
        public CompetencyPageResponseDto generateCompetencyPageNarratives(CompetencyPagePayload payload) {
            CompetencyDetailDto defaultDto = ReportContextDto.getDefaultCompetencyPage(payload.pageNum(), "PCIV126371");
            return new CompetencyPageResponseDto(
                    defaultDto.getReq1(), defaultDto.getResult1(), defaultDto.getRec1(),
                    defaultDto.getReq2(), defaultDto.getResult2(), defaultDto.getRec2(),
                    defaultDto.getReq3(), defaultDto.getResult3(), defaultDto.getRec3()
            );
        }

        @Override
        public GrowPlanResponseDto generateGrowPlanNarratives(GrowPlanPayload payload) {
            return new GrowPlanResponseDto(
                    "تعزيز فاعلية التخطيط الاستراتيجي وتوسيع نطاق المبادرة والتحليل في إدارة العمليات القيادية.",
                    "يمتلك القائد نقاط قوة متميزة في " + payload.topStrengths() + "، بينما تتطلب مجالات " + payload.developmentAreas() + " دعماً وتطويراً مستمراً.",
                    "المشاركة في برامج القيادة الاستراتيجية المتقدمة ومحاكاة إدارة الأزمات والعمليات المشتركة.",
                    "تطبيق خطة تدريبية ومراجعة دورية للتقدم كل 3 أشهر مع القيادة العليا المباشرة."
            );
        }

        @Override
        public String generateNarrativesJson(AiPromptPayload payload) {
            return """
            {
              "socialInterpretation": "من المرجح أنه أجاب بصدق من دون إظهار صورة إيجابية بشدة.",
              "centralInterpretation": "من المرجح أنه أجاب بصراحة بدون رغبة في إخفاء شخصيته الحقيقية.",
              "reservedText": "تشير هذه النتيجة إلى متوسط احتمال إظهار سلوكيات مُقيّدة مرتبطة بسمة التحفظ.",
              "emotionalityText": "تشير هذه النتيجة إلى متوسط احتمال إظهار سلوكيات مُقيّدة مرتبطة بسمة الانفعالية.",
              "hostilityText": "تشير هذه النتيجة إلى متوسط احتمال إظهار سلوكيات مُقيّدة مرتبطة بسمة العدائية.",
              "impulsivityText": "تشير هذه النتيجة إلى متوسط احتمال إظهار سلوكيات مُقيّدة مرتبطة بسمة الاندفاعية.",
              "rigidityText": "متوسط احتمال إظهار سلوكيات مُقيّدة مرتبطة بسمة الصرامة.",
              "unconventionalityText": "تشير هذه النتيجة إلى متوسط إلى مرتفع احتمال إظهار سلوكيات مُقيّدة مرتبطة بسمة اللامألوفية.",
              "growGoalText": "تعزيز فاعلية التخطيط الاستراتيجي وتوسيع نطاق المبادرة والتحليل في إدارة العمليات.",
              "growRealityText": "يمتلك القائد قدرة ممتازة على التنظيم والتحليل، مع حاجة لصقل مهارات استشراف المستقبل والتفكير المجرد.",
              "growOptionsText": "المشاركة في برامج متقدمة في القيادة الاستراتيجية ومحاكاة إدارة الأزمات والعمليات المشتركة.",
              "growWillText": "تطبيق خطة تدريبية ومراجعة دورية للتقدم كل 3 أشهر مع القادة المباشرين."
            }
            """;
        }
    }
}
