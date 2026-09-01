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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

    public LeadershipReportGeneratorService(ObjectMapper objectMapper, Optional<AiReportClient> aiClient) {
        this.objectMapper = objectMapper.copy()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.aiClient = aiClient.orElseGet(DefaultMockAiClient::new);
    }

    /**
     * Main entrypoint: generates a fully populated {@link ReportContextDto} from raw scores.
     *
     * @param rawScore the raw scoring response DTO from the assessment engine
     * @return fully populated ReportContextDto ready for Thymeleaf / PDF rendering
     */
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

        // 6. Build AI Prompt Payload
        AiPromptPayload aiPayload = buildAiPromptPayload(rawScore, report);

        // 7. Invoke AI Engine for Arabic Narratives
        try {
            String aiJsonRaw = aiClient.generateNarrativesJson(aiPayload);
            AiNarrativeResponseDto narratives = parseAiResponse(aiJsonRaw);

            // 8. Merge AI Narratives into the Report Context
            mergeNarratives(report, narratives);
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

        // 2. Behavioral Competencies
        int comm = scaleTo5(findTraitScorePct(traitMap, raw.getTraitScores(), "COMMUNICATION_AND_INFLUENCE", 1, 50.0));
        report.setCommScore(comm);
        report.setCommColor(determineColor(comm, ROLE_BENCHMARKS.get("COMMUNICATION_AND_INFLUENCE")));

        int init = scaleTo5(findTraitScorePct(traitMap, raw.getTraitScores(), "INITIATIVE", 2, 50.0));
        report.setInitiativeScore(init);
        report.setInitiativeColor(determineColor(init, ROLE_BENCHMARKS.get("INITIATIVE")));

        int dec = scaleTo5(findTraitScorePct(traitMap, raw.getTraitScores(), "DECISION_MAKING_AND_RESPONSIBILITY", 3, 50.0));
        report.setDecisionScore(dec);
        report.setDecisionColor(determineColor(dec, ROLE_BENCHMARKS.get("DECISION_MAKING_AND_RESPONSIBILITY")));

        int lead = scaleTo5(findTraitScorePct(traitMap, raw.getTraitScores(), "INSPIRING_LEADERSHIP", 4, 50.0));
        report.setLeadershipScore(lead);
        report.setLeadershipColor(determineColor(lead, ROLE_BENCHMARKS.get("INSPIRING_LEADERSHIP")));

        int strat = scaleTo5(findTraitScorePct(traitMap, raw.getTraitScores(), "STRATEGIC_THINKING", 5, 50.0));
        report.setStrategicScore(strat);
        report.setStrategicColor(determineColor(strat, ROLE_BENCHMARKS.get("STRATEGIC_THINKING")));

        int skills = scaleTo5(findTraitScorePct(traitMap, raw.getTraitScores(), "SKILL_DEVELOPMENT", 6, 50.0));
        report.setSkillsScore(skills);
        report.setSkillsColor(determineColor(skills, ROLE_BENCHMARKS.get("SKILL_DEVELOPMENT")));

        int adapt = scaleTo5(findTraitScorePct(traitMap, raw.getTraitScores(), "ADAPTABILITY", 7, 50.0));
        report.setAdaptabilityScore(adapt);
        report.setAdaptabilityColor(determineColor(adapt, ROLE_BENCHMARKS.get("ADAPTABILITY")));

        int plan = scaleTo5(findTraitScorePct(traitMap, raw.getTraitScores(), "SYSTEMATIC_ANALYSIS_AND_PLANNING", 8, 50.0));
        report.setAnalysisScore(plan);
        report.setAnalysisColor(determineColor(plan, ROLE_BENCHMARKS.get("SYSTEMATIC_ANALYSIS_AND_PLANNING")));

        // 3. Cognitive Abilities (GCAT)
        Map<GcatSubtestCode, Double> gcatMap = new HashMap<>();
        if (raw.getGcatSubtestScores() != null) {
            for (var gs : raw.getGcatSubtestScores()) {
                if (gs.getSubtest() != null && gs.getScorePct() != null) {
                    gcatMap.put(gs.getSubtest(), gs.getScorePct());
                }
            }
        }

        int abs = scaleTo5(gcatMap.getOrDefault(GcatSubtestCode.ABSTRACT, 50.0));
        report.setAbstractScore(abs);
        report.setAbstractColor(determineColor(abs, ROLE_BENCHMARKS.get("ABSTRACT")));

        int num = scaleTo5(gcatMap.getOrDefault(GcatSubtestCode.NUMERICAL, 50.0));
        report.setNumericalScore(num);
        report.setNumericalColor(determineColor(num, ROLE_BENCHMARKS.get("NUMERICAL")));

        int verb = scaleTo5(gcatMap.getOrDefault(GcatSubtestCode.VERBAL, 50.0));
        report.setVerbalScore(verb);
        report.setVerbalColor(determineColor(verb, ROLE_BENCHMARKS.get("VERBAL")));
        report.setGeneralAbilitiesColor(determineColor((abs + num + verb) / 3, 4));
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

    public record AiPromptPayload(
            String candidateId,
            String candidateName,
            Map<String, Object> normalizedScores,
            Map<String, Integer> roleBenchmarks,
            List<String> competencyNames,
            String instructions
    ) {}

    private AiPromptPayload buildAiPromptPayload(AssessmentScoreResponseDto raw, ReportContextDto report) {
        Map<String, Object> scores = new LinkedHashMap<>();
        scores.put("socialScore", report.getSocialScore());
        scores.put("centralScore", report.getCentralScore());
        scores.put("reservedScore", report.getReservedScore());
        scores.put("emotionalityScore", report.getEmotionalityScore());
        scores.put("hostilityScore", report.getHostilityScore());
        scores.put("impulsivityScore", report.getImpulsivityScore());
        scores.put("rigidityScore", report.getRigidityScore());
        scores.put("unconventionalityScore", report.getUnconventionalityScore());
        scores.put("communicationScore", report.getCommScore());
        scores.put("initiativeScore", report.getInitiativeScore());
        scores.put("decisionScore", report.getDecisionScore());
        scores.put("leadershipScore", report.getLeadershipScore());
        scores.put("strategicThinkingScore", report.getStrategicScore());
        scores.put("skillDevelopmentScore", report.getSkillsScore());
        scores.put("adaptabilityScore", report.getAdaptabilityScore());
        scores.put("systematicAnalysisScore", report.getAnalysisScore());

        List<String> competencies = List.of(
                "التواصل والتأثير الفعال",
                "المبادرة",
                "اتخاذ القرار وتحمل المسؤولية",
                "القيادة الملهمة",
                "التفكير الاستراتيجي",
                "تطوير المهارات",
                "القدرة على التكيف",
                "التحليل والتخطيط المنهجي"
        );

        String instructions = """
                أنت خبير قيادي ومستشار في القياس النفسي (Psychometric Executive Coach).
                بناءً على الدرجات المعيارية المرفقة (1-10 لسمات الشخصية والموانع، و 1-5 للكفاءات القيادية والقدرات)،
                قم بصياغة سرد تحليلي وتوصيات تطويرية مهنية باللغة العربية الفصحى.
                يجب أن يكون الإخراج كائن JSON مصغر وصالح فقط بدون أي كتل markdown أو شروحات إضافية.
                الالتزام التام بالمفاتيح المحددة في المخطط:
                - socialInterpretation
                - centralInterpretation
                - reservedText, emotionalityText, hostilityText, impulsivityText, rigidityText, unconventionalityText
                - competencyNarratives: خريطة بأرقام الصفحات من 7 إلى 14، يحتوي كل عنصر على (result1, rec1, result2, rec2, result3, rec3 - مع مراعاة أن صفحة 11 تتكون من بندين فقط).
                - growGoalText, growRealityText, growOptionsText, growWillText
                """;

        return new AiPromptPayload(
                report.getCandidateId(),
                report.getCandidateName(),
                scores,
                ROLE_BENCHMARKS,
                competencies,
                instructions
        );
    }

    // =========================================================================
    // STEP 4 & 5: Deserialization & Narrative Merging
    // =========================================================================

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

    private AiNarrativeResponseDto parseAiResponse(String jsonString) {
        if (jsonString == null || jsonString.isBlank()) {
            return new AiNarrativeResponseDto();
        }

        try {
            // Clean markdown code blocks if the LLM wrapped response in ```json ... ```
            String cleanJson = jsonString.trim();
            if (cleanJson.startsWith("```json")) {
                cleanJson = cleanJson.substring(7);
            } else if (cleanJson.startsWith("```")) {
                cleanJson = cleanJson.substring(3);
            }
            if (cleanJson.endsWith("```")) {
                cleanJson = cleanJson.substring(0, cleanJson.length() - 3);
            }
            cleanJson = cleanJson.trim();

            return objectMapper.readValue(cleanJson, AiNarrativeResponseDto.class);
        } catch (Exception e) {
            log.warn("Failed to parse AI JSON response, falling back to default text: {}", e.getMessage());
            return new AiNarrativeResponseDto();
        }
    }

    private void mergeNarratives(ReportContextDto report, AiNarrativeResponseDto ai) {
        if (ai == null) return;

        // Page 2 Narratives
        if (ai.socialInterpretation != null && !ai.socialInterpretation.isBlank()) {
            report.setSocialInterpretation(ai.socialInterpretation);
        }
        if (ai.centralInterpretation != null && !ai.centralInterpretation.isBlank()) {
            report.setCentralInterpretation(ai.centralInterpretation);
        }

        // Page 4 Derailers
        if (ai.reservedText != null) report.setReservedText(ai.reservedText);
        if (ai.emotionalityText != null) report.setEmotionalityText(ai.emotionalityText);
        if (ai.hostilityText != null) report.setHostilityText(ai.hostilityText);
        if (ai.impulsivityText != null) report.setImpulsivityText(ai.impulsivityText);
        if (ai.rigidityText != null) report.setRigidityText(ai.rigidityText);
        if (ai.unconventionalityText != null) report.setUnconventionalityText(ai.unconventionalityText);

        // Pages 7 to 14 Competency Tables
        if (ai.competencyNarratives != null) {
            for (int p = 7; p <= 14; p++) {
                AiNarrativeResponseDto.PageNarrativeDto pNarrative = ai.competencyNarratives.get(String.valueOf(p));
                if (pNarrative == null) {
                    pNarrative = ai.competencyNarratives.get("page" + p);
                }

                if (pNarrative != null) {
                    CompetencyDetailDto cDto = report.getCompetencyPage(p);
                    if (cDto != null) {
                        if (pNarrative.result1 != null) cDto.setResult1(pNarrative.result1);
                        if (pNarrative.rec1 != null) cDto.setRec1(pNarrative.rec1);
                        if (pNarrative.result2 != null) cDto.setResult2(pNarrative.result2);
                        if (pNarrative.rec2 != null) cDto.setRec2(pNarrative.rec2);
                        if (p != 11) { // Page 11 has only 2 rows
                            if (pNarrative.result3 != null) cDto.setResult3(pNarrative.result3);
                            if (pNarrative.rec3 != null) cDto.setRec3(pNarrative.rec3);
                        }
                    }
                }
            }
        }

        // Page 15: GROW Development Plan
        if (ai.growGoalText != null) report.setGrowGoalText(ai.growGoalText);
        if (ai.growRealityText != null) report.setGrowRealityText(ai.growRealityText);
        if (ai.growOptionsText != null) report.setGrowOptionsText(ai.growOptionsText);
        if (ai.growWillText != null) report.setGrowWillText(ai.growWillText);
    }

    private void applyFallbackNarratives(ReportContextDto report) {
        // Fallback already pre-seeded in default initialization
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
        String generateNarrativesJson(AiPromptPayload payload);
    }

    public static class DefaultMockAiClient implements AiReportClient {
        @Override
        public String generateNarrativesJson(AiPromptPayload payload) {
            // Mock LLM generation returning structured JSON matching Arabic assessment style
            return """
            {
              "socialInterpretation": "من المرجح أنه أجاب بصدق من دون إظهار صورة إيجابية بشدة. ما من إجراءات أخرى يلزم اتخاذها.",
              "centralInterpretation": "من المرجح أنه أجاب بصراحة بدون رغبة في إخفاء شخصيته الحقيقية. ما من إجراءات أخرى يلزم اتخاذها.",
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
