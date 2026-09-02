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
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;
    private final org.springframework.cache.CacheManager cacheManager;

    @org.springframework.beans.factory.annotation.Autowired
    public LeadershipReportGeneratorService(
            ObjectMapper objectMapper,
            @org.springframework.beans.factory.annotation.Autowired(required = false) org.springframework.jdbc.core.JdbcTemplate jdbcTemplate,
            @org.springframework.beans.factory.annotation.Autowired(required = false) org.springframework.cache.CacheManager cacheManager
    ) {
        this.objectMapper = objectMapper.copy()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.jdbcTemplate = jdbcTemplate;
        this.cacheManager = cacheManager;
    }

    public LeadershipReportGeneratorService(ObjectMapper objectMapper) {
        this(objectMapper, null, null);
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

            List<String> aiCaches = List.of();
            for (String cName : aiCaches) {
                org.springframework.cache.Cache c = cacheManager.getCache(cName);
                if (c != null) {
                    c.clear();
                }
            }
        }
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

        // 5. Populate Competency Data Statically (Pages 7-14)
        Map<String, Double> traitMap = new HashMap<>();
        if (rawScore.getTraitScores() != null) {
            for (var ts : rawScore.getTraitScores()) {
                if (ts.getTraitCode() != null && ts.getScorePct() != null) {
                    traitMap.put(ts.getTraitCode().trim(), ts.getScorePct());
                }
            }
        }

        populateCompetencyData(report, 7, "COMMUNICATION_AND_INFLUENCE", traitMap, rawScore.getTraitScores(), report.getCommColor());
        populateCompetencyData(report, 8, "INITIATIVE", traitMap, rawScore.getTraitScores(), report.getInitiativeColor());
        populateCompetencyData(report, 9, "DECISION_MAKING_AND_RESPONSIBILITY", traitMap, rawScore.getTraitScores(), report.getDecisionColor());
        populateCompetencyData(report, 10, "INSPIRING_LEADERSHIP", traitMap, rawScore.getTraitScores(), report.getLeadershipColor());
        populateCompetencyData(report, 11, "STRATEGIC_THINKING", traitMap, rawScore.getTraitScores(), report.getStrategicColor());
        populateCompetencyData(report, 12, "SKILL_DEVELOPMENT", traitMap, rawScore.getTraitScores(), report.getSkillsColor());
        populateCompetencyData(report, 13, "ADAPTABILITY", traitMap, rawScore.getTraitScores(), report.getAdaptabilityColor());
        populateCompetencyData(report, 14, "SYSTEMATIC_ANALYSIS_AND_PLANNING", traitMap, rawScore.getTraitScores(), report.getAnalysisColor());

        // Populate Static Narratives for Page 2, 4, and 15
        populateStaticNarratives(rawScore, report);

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


    private void populateStaticNarratives(AssessmentScoreResponseDto raw, ReportContextDto report) {
        // Page 2
        report.setSocialInterpretation(report.getSocialScore() > 7 
            ? "تشير الدرجة إلى ميل ملحوظ لإظهار صورة إيجابية مثالية (خطر مرتفع). يُنصح بمراعاة ذلك عند تفسير نتائج التقييم الأخرى."
            : "من المرجح أنه أجاب بصدق وموضوعية دون تزييف مفرط للصورة الإيجابية (خطر " + report.getSocialRisk() + "). لا توجد مؤشرات مقلقة.");
        report.setCentralInterpretation(report.getCentralScore() > 7
            ? "لوحظ ميل مرتفع لاختيار الإجابات الوسطية وتجنب إبداء مواقف واضحة وحاسمة (خطر مرتفع)."
            : "أظهر المرشح وضوحاً وحسماً في تحديد مواقفه دون اللجوء المفرط للإجابات المحايدة (خطر " + report.getCentralRisk() + ").");

        // Page 4
        report.setReservedText("تشير نتيجة (" + report.getReservedScore() + "/10) إلى احتمالية التحفظ والانغلاق تحت الضغط، مما قد يؤثر على التواصل مع الفريق.");
        report.setEmotionalityText("تشير نتيجة (" + report.getEmotionalityScore() + "/10) إلى درجة التحكم الانفعالي، مع الحاجة للمحافظة على الثبات في المواقف الحرجة.");
        report.setHostilityText("تشير نتيجة (" + report.getHostilityScore() + "/10) إلى مستوى التنافسية وإمكانية إظهار حدة في التعامل عند النزاعات الحادة.");
        report.setImpulsivityText("تشير نتيجة (" + report.getImpulsivityScore() + "/10) إلى سرعة اتخاذ الإجراءات مع احتمالية التسرع قبل استكمال دراسة البدائل تحت وطأة الوقت.");
        report.setRigidityText("تشير نتيجة (" + report.getRigidityScore() + "/10) إلى التمسك بالقواعد والإجراءات المعمول بها مع حاجة لمرونة إضافية عند تغير الظروف.");
        report.setUnconventionalityText("تشير نتيجة (" + report.getUnconventionalityScore() + "/10) إلى الميل لتبني أساليب غير تقليدية ومبتكرة في معالجة التحديات القيادية.");

        // Page 15
        report.setGrowGoalText("تعزيز فاعلية التخطيط الاستراتيجي وتوسيع نطاق المبادرة والتحليل في إدارة العمليات القيادية.");
        report.setGrowRealityText("يمتلك القائد نقاط قوة متميزة في " + extractTopStrengths(report) + "، بينما تتطلب مجالات " + extractDevelopmentAreas(report) + " دعماً وتطويراً مستمراً.");
        report.setGrowOptionsText("المشاركة في برامج القيادة الاستراتيجية المتقدمة ومحاكاة إدارة الأزمات والعمليات المشتركة.");
        report.setGrowWillText("تطبيق خطة تدريبية ومراجعة دورية للتقدم كل 3 أشهر مع القيادة العليا المباشرة.");
    }

    private String extractTopStrengths(ReportContextDto report) {
        Map<String, Double> compScores = new java.util.LinkedHashMap<>();
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
                .map(e -> String.format(java.util.Locale.US, "%s (%.1f/5)", e.getKey(), e.getValue()))
                .reduce((a, b) -> a + "، " + b)
                .orElse("التحليل والتخطيط المنهجي");
    }

    private String extractDevelopmentAreas(ReportContextDto report) {
        Map<String, Double> compScores = new java.util.LinkedHashMap<>();
        compScores.put("التواصل والتأثير الفعال", report.getCommScore());
        compScores.put("المبادرة", report.getInitiativeScore());
        compScores.put("اتخاذ القرار وتحمل المسؤولية", report.getDecisionScore());
        compScores.put("القيادة الملهمة", report.getLeadershipScore());
        compScores.put("التفكير الاستراتيجي", report.getStrategicScore());
        compScores.put("تطوير المهارات", report.getSkillsScore());
        compScores.put("القدرة على التكيف", report.getAdaptabilityScore());
        compScores.put("التحليل والتخطيط المنهجي", report.getAnalysisScore());

        return compScores.entrySet().stream()
                .sorted(java.util.Map.Entry.comparingByValue())
                .limit(2)
                .map(e -> String.format(java.util.Locale.US, "%s (%.1f/5)", e.getKey(), e.getValue()))
                .reduce((a, b) -> a + "، " + b)
                .orElse("التفكير الاستراتيجي");
    }

    private void populateCompetencyData(ReportContextDto report, int pageNum, String competencyCode, Map<String, Double> traitMap, List<AssessmentScoreResponseDto.TraitScoreDto> traitScores, String competencyColor) {
        CompetencyDetailDto dto = new CompetencyDetailDto();
        dto.setPageNum(pageNum);
        dto.setCandidateId(report.getCandidateId());
        
        int expectedDisplayOrder = pageNum - 6;
        double dScore = scaleTo5Double(findTraitScorePct(traitMap, traitScores, competencyCode, expectedDisplayOrder, 50.0));
        dto.setCompetencyScore(dScore);
        dto.setCompetencyColor(competencyColor);

        String colorIndicator = dScore >= 4.0 ? "#388e3c" : (dScore >= 3.0 ? "#d97736" : "#d32f2f");
        dto.setIndicator1Color(colorIndicator);
        dto.setIndicator2Color(colorIndicator);
        dto.setIndicator3Color(colorIndicator);

        switch (competencyCode) {
            case "COMMUNICATION_AND_INFLUENCE":
                dto.setCompetencyTitle("التواصل والتأثير الفعال");
                dto.setCompetencyDesc("قدرة القائد على نقل الأفكار والمعلومات بوضوح وإقناع، والتأثير الإيجابي في الآخرين، وبناء علاقات عمل قوية تدعم تحقيق أهداف المنظمة.");
                
                dto.setReq1("نقل الرسائل المعقدة بوضوح وشفافية.");
                if (dScore >= 4.0) { dto.setResult1("مهارات استثنائية في تبسيط المعلومات."); dto.setRec1("قيادة العروض الاستراتيجية."); }
                else if (dScore >= 3.0) { dto.setResult1("ينقل المعلومات جيداً في المواقف المعتادة."); dto.setRec1("التدرب على صياغة الرسائل المعقدة."); }
                else { dto.setResult1("يواجه صعوبة في إيصال أفكاره بوضوح."); dto.setRec1("برامج تدريبية في التواصل الفعال."); }

                dto.setReq2("الاستماع النشط لاستيعاب وجهات النظر.");
                if (dScore >= 4.0) { dto.setResult2("قدرة عالية على الاستماع وفهم الدوافع."); dto.setRec2("إدارة النزاعات المعقدة."); }
                else if (dScore >= 3.0) { dto.setResult2("يستمع جيداً لكنه قد يتسرع في الحلول."); dto.setRec2("ممارسة الاستماع التأملي."); }
                else { dto.setResult2("يميل للمقاطعة والتركيز على الرد."); dto.setRec2("تطبيق تمارين الوعي الذاتي."); }

                dto.setReq3("إقناع الآخرين والتفاوض الدبلوماسي.");
                if (dScore >= 4.0) { dto.setResult3("مفاوض بارع يبني توافقاً بسلاسة."); dto.setRec3("تمثيل المؤسسة في شراكات استراتيجية."); }
                else if (dScore >= 3.0) { dto.setResult3("يقنع بالبيانات لكنه يواجه صعوبة مع المقاومة."); dto.setRec3("تطوير استراتيجيات التفاوض."); }
                else { dto.setResult3("يفتقر للمرونة ويميل لفرض الرأي."); dto.setRec3("ورش عمل في الإقناع الدبلوماسي."); }
                break;

            case "INITIATIVE":
                dto.setCompetencyTitle("المبادرة");
                dto.setCompetencyDesc("قدرة القائد على التحرك من تلقاء نفسه دون انتظار الأوامر، عبر اقتراح الأفكار واتخاذ الإجراءات المناسبة في الوقت المناسب لتحسين العمل وحل المشكلات وتحقيق الأهداف بسرعة وفعالية.");

                dto.setReq1("الاستباقية في تحديد وحل المشكلات.");
                if (dScore >= 4.0) { dto.setResult1("يتنبأ بالمشكلات ويعالجها قبل تفاقمها."); dto.setRec1("قيادة فرق الابتكار الاستباقي."); }
                else if (dScore >= 3.0) { dto.setResult1("يحل المشكلات عند ظهورها بفاعلية."); dto.setRec1("تطوير التفكير الاستباقي."); }
                else { dto.setResult1("ينتظر التوجيهات للتعامل مع التحديات."); dto.setRec1("تحفيز الاستقلالية في العمل."); }

                dto.setReq2("تجاوز الأهداف وتحمل المخاطر المحسوبة.");
                if (dScore >= 4.0) { dto.setResult2("يتخطى الأهداف باستمرار ويدير المخاطر ببراعة."); dto.setRec2("إدارة مشاريع عالية المخاطر."); }
                else if (dScore >= 3.0) { dto.setResult2("يحقق الأهداف المطلوبة بدقة والتزام."); dto.setRec2("تشجيع الخروج عن المألوف."); }
                else { dto.setResult2("يكتفي بالحد الأدنى ويتجنب المخاطرة."); dto.setRec2("بناء الثقة لتحمل مسؤوليات أكبر."); }

                dto.setReq3("استغلال الفرص دون توجيه مباشر.");
                if (dScore >= 4.0) { dto.setResult3("يقتنص الفرص ويحولها لنجاحات مؤسسية."); dto.setRec3("المشاركة في التخطيط التوسعي."); }
                else if (dScore >= 3.0) { dto.setResult3("يستغل الفرص الواضحة والمباشرة."); dto.setRec3("التدرب على رصد الفرص الخفية."); }
                else { dto.setResult3("يفوت الفرص لتردده في اتخاذ الخطوة الأولى."); dto.setRec3("تعزيز الجرأة المهنية."); }
                break;

            case "DECISION_MAKING_AND_RESPONSIBILITY":
                dto.setCompetencyTitle("اتخاذ القرار وتحمل المسؤولية");
                dto.setCompetencyDesc("القدرة على جمع وتحليل المعلومات لتقييم الخيارات المتاحة واستنتاج حلول عملية، واتخاذ القرارات في الوقت المناسب مع تحمل المسؤولية الكاملة عن نتائجها.");

                dto.setReq1("جمع وتحليل البيانات قبل اتخاذ القرار.");
                if (dScore >= 4.0) { dto.setResult1("يحلل المعطيات بعمق ويتخذ قرارات مبنية على أدلة."); dto.setRec1("تصميم أنظمة دعم القرار."); }
                else if (dScore >= 3.0) { dto.setResult1("يعتمد على البيانات المتاحة بحدود معقولة."); dto.setRec1("توسيع نطاق مصادر المعلومات."); }
                else { dto.setResult1("يتخذ قرارات متسرعة دون تحليل كافٍ."); dto.setRec1("التدريب على التحليل الإحصائي المبسط."); }

                dto.setReq2("الحسم في المواقف المعقدة والحرجة.");
                if (dScore >= 4.0) { dto.setResult2("سريع وحاسم في الأزمات بثقة عالية."); dto.setRec2("إدارة غرف الأزمات."); }
                else if (dScore >= 3.0) { dto.setResult2("يتخذ قرارات جيدة لكنه يتردد تحت الضغط."); dto.setRec2("محاكاة سيناريوهات الأزمات."); }
                else { dto.setResult2("يتجنب اتخاذ القرارات في المواقف الصعبة."); dto.setRec2("برامج بناء الثقة القيادية."); }

                dto.setReq3("تحمل التبعات والمساءلة الكاملة.");
                if (dScore >= 4.0) { dto.setResult3("يتحمل المسؤولية كاملة عن نتائج فريقه."); dto.setRec3("تولي قيادة إدارات متعثرة."); }
                else if (dScore >= 3.0) { dto.setResult3("يتحمل المسؤولية في المواقف المعتادة."); dto.setRec3("تعزيز ثقافة الشفافية المطلقة."); }
                else { dto.setResult3("يميل لإلقاء اللوم على الظروف أو الآخرين."); dto.setRec3("جلسات توجيه حول أخلاقيات القيادة."); }
                break;

            case "INSPIRING_LEADERSHIP":
                dto.setCompetencyTitle("القيادة الملهمة");
                dto.setCompetencyDesc("القدرة على إلهام وتوجيه الآخرين نحو تحقيق الرؤية المشتركة، من خلال بناء الثقة، وتحفيز الأداء، وتمكين الأفراد، وخلق بيئة عمل تشجع على التميز والابتكار.");

                dto.setReq1("بناء الثقة وتمكين أعضاء الفريق.");
                if (dScore >= 4.0) { dto.setResult1("يخلق بيئة عمل قائمة على الثقة والتمكين."); dto.setRec1("إعداد برامج تطوير القادة."); }
                else if (dScore >= 3.0) { dto.setResult1("يفوض المهام لكن يحتفظ بالرقابة الدقيقة."); dto.setRec1("منح استقلالية أكبر للفريق."); }
                else { dto.setResult1("يمركز الصلاحيات ويقلل من ثقة فريقه."); dto.setRec1("التدريب على التفويض الفعال."); }

                dto.setReq2("تحفيز الآخرين نحو الرؤية المشتركة.");
                if (dScore >= 4.0) { dto.setResult2("يلهم فريقه لتحقيق مستويات أداء استثنائية."); dto.setRec2("قيادة مبادرات التغيير المؤسسي."); }
                else if (dScore >= 3.0) { dto.setResult2("يحفز فريقه بشكل روتيني عبر الحوافز التقليدية."); dto.setRec2("تطوير أساليب تحفيز معنوية."); }
                else { dto.setResult2("يفتقر لأساليب التحفيز مما يؤدي لانخفاض الروح المعنوية."); dto.setRec2("تطوير مهارات الذكاء العاطفي."); }

                dto.setReq3("تقديم الدعم والتوجيه المستمر.");
                if (dScore >= 4.0) { dto.setResult3("موجه بارع يساهم في بناء صف ثانٍ من القادة."); dto.setRec3("تأسيس برنامج إرشاد داخلي."); }
                else if (dScore >= 3.0) { dto.setResult3("يقدم توجيهاً عند الطلب فقط."); dto.setRec3("جدولة جلسات توجيه دورية."); }
                else { dto.setResult3("يتجاهل دور التوجيه ويركز على التنفيذ فقط."); dto.setRec3("برامج إعداد الموجهين."); }
                break;

            case "STRATEGIC_THINKING":
                dto.setCompetencyTitle("التفكير الاستراتيجي");
                dto.setCompetencyDesc("القدرة على استيعاب الصورة الكلية والتوجهات المستقبلية، وتحليل البيئة الداخلية والخارجية لصياغة رؤى استراتيجية تدعم تحقيق الأهداف طويلة المدى للمنظمة.");

                dto.setReq1("فهم الصورة الكلية وتحليل البيئة المحيطة.");
                if (dScore >= 4.0) { dto.setResult1("يمتلك رؤية شمولية ويربط الأحداث ببراعة."); dto.setRec1("صياغة الاستراتيجيات المؤسسية."); }
                else if (dScore >= 3.0) { dto.setResult1("يفهم التوجهات العامة دون الغوص في الروابط المعقدة."); dto.setRec1("التدريب على النماذج الاستراتيجية."); }
                else { dto.setResult1("يغرق في التفاصيل التشغيلية ويفقد الصورة الكبرى."); dto.setRec1("التركيز على مهارات التفكير التجريدي."); }

                dto.setReq2("استشراف التحديات والفرص المستقبلية.");
                if (dScore >= 4.0) { dto.setResult2("يتوقع التحولات المستقبلية ويستعد لها."); dto.setRec2("إدارة لجان المخاطر الاستراتيجية."); }
                else if (dScore >= 3.0) { dto.setResult2("يتجاوب مع المتغيرات بخطط قصيرة المدى."); dto.setRec2("تطوير مهارات التخطيط بعيد المدى."); }
                else { dto.setResult2("يتفاجأ بالتغيرات ويفتقر للرؤية الاستباقية."); dto.setRec2("الورش التفاعلية لاستشراف المستقبل."); }

                dto.setReq3("مواءمة الخطط مع رسالة المنظمة.");
                if (dScore >= 4.0) { dto.setResult3("يضمن انسجام كافة المبادرات مع الأهداف العليا."); dto.setRec3("الإشراف على محافظ المشاريع الاستراتيجية."); }
                else if (dScore >= 3.0) { dto.setResult3("يوائم الأهداف الأساسية بفاعلية مقبولة."); dto.setRec3("مراجعة مؤشرات الأداء الاستراتيجية."); }
                else { dto.setResult3("ينفذ خططاً تتعارض أحياناً مع التوجه العام."); dto.setRec3("تحديث الفهم للرؤية المؤسسية."); }
                break;

            case "SKILL_DEVELOPMENT":
                dto.setCompetencyTitle("تطوير المهارات");
                dto.setCompetencyDesc("القدرة على تحديد الاحتياجات التدريبية لنفسه وللآخرين، والسعي المستمر لاكتساب معارف ومهارات جديدة، وتوفير التوجيه والدعم لرفع مستوى الكفاءة والأداء العام.");

                dto.setReq1("تحديد فجوات الأداء والوعي بالقدرات.");
                if (dScore >= 4.0) { dto.setResult1("وعي ذاتي عالٍ وسعي دائم للتميز."); dto.setRec1("مشاركة تجارب النجاح مع القيادات."); }
                else if (dScore >= 3.0) { dto.setResult1("يدرك قدراته بشكل عام ويسعى للتطوير عند الحاجة."); dto.setRec1("تصميم خطة تطوير شخصية سنوية."); }
                else { dto.setResult1("يبالغ في تقدير قدراته ويتجاهل فجوات الأداء."); dto.setRec1("تقييم 360 درجة لزيادة الوعي."); }

                dto.setReq2("توجيه الزملاء ونقل الخبرات.");
                if (dScore >= 4.0) { dto.setResult2("يشارك المعرفة بسخاء ويبني قدرات فريقه."); dto.setRec2("قيادة مجتمعات الممارسة الداخلية."); }
                else if (dScore >= 3.0) { dto.setResult2("يشارك المعلومات المباشرة المتعلقة بالعمل."); dto.setRec2("تشجيع توثيق الدروس المستفادة."); }
                else { dto.setResult2("يحتفظ بالمعلومات لنفسه كمصدر للقوة."); dto.setRec2("ربط تقييم الأداء بنقل المعرفة."); }

                dto.setReq3("مواكبة المتغيرات والتعلم المستمر.");
                if (dScore >= 4.0) { dto.setResult3("يتبنى عقلية التعلم المستمر ويواكب أحدث الممارسات."); dto.setRec3("تمثيل المؤسسة في مؤتمرات تخصصية."); }
                else if (dScore >= 3.0) { dto.setResult3("يتعلم مهارات جديدة عندما يتطلب العمل ذلك."); dto.setRec3("توسيع نطاق القراءة والاطلاع التخصصي."); }
                else { dto.setResult3("يقاوم تعلم مهارات جديدة ويعتمد على خبراته القديمة."); dto.setRec3("التكليف بمهام تتطلب تقنيات حديثة."); }
                break;

            case "ADAPTABILITY":
                dto.setCompetencyTitle("القدرة على التكيف");
                dto.setCompetencyDesc("القدرة على التعامل بمرونة وإيجابية مع التغييرات والمواقف الغامضة أو الضاغطة، وتعديل خطط العمل لتتناسب مع متطلبات البيئة المتغيرة، دون المساس بجودة الأداء.");

                dto.setReq1("المرونة في التعامل مع المتغيرات والمفاجآت.");
                if (dScore >= 4.0) { dto.setResult1("يتكيف بسرعة فائقة مع التغيرات ويحولها لصالحه."); dto.setRec1("قيادة الفِرَق في بيئات عالية التقلب."); }
                else if (dScore >= 3.0) { dto.setResult1("يتقبل التغيير بعد فترة قصيرة من التكيف."); dto.setRec1("المشاركة في مشاريع رشيقة (Agile)."); }
                else { dto.setResult1("يقاوم التغيير بشدة ويتمسك بالروتين."); dto.setRec1("التدريب على إدارة التغيير."); }

                dto.setReq2("العمل بفاعلية تحت الضغط والغموض.");
                if (dScore >= 4.0) { dto.setResult2("يحافظ على هدوئه وإنتاجيته في أقصى درجات الضغط."); dto.setRec2("تولي مهام إنقاذ المشاريع المتعثرة."); }
                else if (dScore >= 3.0) { dto.setResult2("يدير الضغوط المعتادة بكفاءة، لكنه يتوتر في الأزمات."); dto.setRec2("تمارين إدارة الإجهاد والمرونة النفسية."); }
                else { dto.setResult2("ينهار أداؤه سريعاً عند مواجهة الغموض أو الضغط."); dto.setRec2("توفير بيئة عمل مستقرة وتوجيه مستمر."); }

                dto.setReq3("تبني التغيير المؤسسي وقيادة الآخرين نحوه.");
                if (dScore >= 4.0) { dto.setResult3("عرّاب للتغيير يقود فريقه بسلاسة نحو الرؤية الجديدة."); dto.setRec3("إدارة برامج التحول المؤسسي."); }
                else if (dScore >= 3.0) { dto.setResult3("يدعم التغيير لكنه لا يأخذ دور المبادرة في قيادته."); dto.setRec3("تولي دور سفير التغيير في إدارته."); }
                else { dto.setResult3("يبث السلبية تجاه المبادرات الجديدة."); dto.setRec3("جلسات توجيه حول أهمية المواكبة الاستراتيجية."); }
                break;

            case "SYSTEMATIC_ANALYSIS_AND_PLANNING":
                dto.setCompetencyTitle("التحليل والتخطيط المنهجي");
                dto.setCompetencyDesc("القدرة على دراسة المشكلات بعمق، ووضع خطط عمل مفصلة وممنهجة تتضمن ترتيب الأولويات وتوزيع الموارد بكفاءة، مع متابعة التنفيذ لضمان تحقيق الأهداف المحددة بدقة.");

                dto.setReq1("تحليل المشكلات المعقدة وتقسيمها منطقياً.");
                if (dScore >= 4.0) { dto.setResult1("يحلل جذور المشكلات بأسلوب علمي ومنهجي دقيق."); dto.setRec1("قيادة لجان التحقيق في التحديات المؤسسية."); }
                else if (dScore >= 3.0) { dto.setResult1("يحلل المشكلات السطحية جيداً."); dto.setRec1("التدرب على أدوات تحليل السبب الجذري."); }
                else { dto.setResult1("يعالج الأعراض ويتجاهل الأسباب الحقيقية."); dto.setRec1("التدريب على التفكير المنطقي."); }

                dto.setReq2("وضع خطط تنفيذية دقيقة وقابلة للقياس.");
                if (dScore >= 4.0) { dto.setResult2("يصمم خططاً محكمة تراعي أدق تفاصيل التنفيذ."); dto.setRec2("إدارة مكاتب المشاريع (PMO)."); }
                else if (dScore >= 3.0) { dto.setResult2("يضع خططاً جيدة تنقصها أحياناً بعض التفاصيل الدقيقة."); dto.setRec2("استخدام برمجيات إدارة المشاريع المتقدمة."); }
                else { dto.setResult2("يعمل بعشوائية ويفتقر للتخطيط المسبق."); dto.setRec2("دورة مكثفة في إدارة المشاريع الأساسية."); }

                dto.setReq3("تحديد الأولويات وتخصيص الموارد بكفاءة.");
                if (dScore >= 4.0) { dto.setResult3("يدير الموارد ببراعة ويحقق أقصى عائد بأقل تكلفة."); dto.setRec3("الإشراف على الميزانيات التشغيلية الكبرى."); }
                else if (dScore >= 3.0) { dto.setResult3("يخصص الموارد بشكل مقبول مع بعض الهدر."); dto.setRec3("التدريب على إدارة الموارد الرشيقة."); }
                else { dto.setResult3("يستنزف الموارد ويواجه صعوبة في ترتيب الأولويات."); dto.setRec3("تطبيق مصفوفات إدارة الوقت والأولويات."); }
                break;
        }

        report.getCompetencyPages().put(pageNum, dto);
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
}
