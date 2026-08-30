package com.psychometric.platform.features.itembank.gcat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.gcat.entity.*;
import com.psychometric.platform.features.itembank.gcat.repository.GcatOptionRepository;
import com.psychometric.platform.features.itembank.gcat.repository.GcatQuestionRepository;
import com.psychometric.platform.features.itembank.gcat.repository.GcatSubtestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Seeds the GCAT (Cognitive Abilities Test) item bank from gcat_items.json.
 * Runs automatically on startup if the gcat_questions table is empty.
 * To re-seed from scratch: truncate gcat_options, gcat_questions, gcat_subtests — then restart the app.
 */
@Component
@Order(2)
public class GcatDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(GcatDataSeeder.class);

    private final GcatSubtestRepository subtestRepository;
    private final GcatQuestionRepository questionRepository;
    private final GcatOptionRepository optionRepository;
    private final ObjectMapper objectMapper;

    // Subtest metadata: code -> [nameAr, descriptionAr, fullQuota, quickQuota]
    private static final Map<GcatSubtestCode, Object[]> SUBTEST_META = Map.of(
            GcatSubtestCode.ABSTRACT, new Object[]{
                    "التفكير المجرد",
                    "قياس القدرة على التعرف على الأنماط والعلاقات المجردة وتطبيق القواعد المنطقية.",
                    14, 7, 480
            },
            GcatSubtestCode.NUMERICAL, new Object[]{
                    "الاستدلال الكمي",
                    "قياس القدرة على فهم المعلومات الرقمية وتحليلها واستخدامها في الاستنتاج المنطقي.",
                    10, 5, 480
            },
            GcatSubtestCode.VERBAL, new Object[]{
                    "الاستدلال اللفظي",
                    "قياس القدرة على فهم المفاهيم اللفظية والمنطق القائم على اللغة العربية.",
                    10, 5, 480
            }
    );

    public GcatDataSeeder(GcatSubtestRepository subtestRepository,
                          GcatQuestionRepository questionRepository,
                          GcatOptionRepository optionRepository,
                          ObjectMapper objectMapper) {
        this.subtestRepository = subtestRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (questionRepository.count() > 0) {
            log.info("GCAT item bank already seeded ({} questions found). Skipping.", questionRepository.count());
            return;
        }

        log.info(">>> Seeding GCAT item bank from gcat_items.json...");

        // Step 1: Seed subtests
        Map<GcatSubtestCode, GcatSubtest> subtestMap = seedSubtests();

        // Step 2: Load and parse JSON
        ClassPathResource resource = new ClassPathResource("data/gcat_items.json");
        InputStream inputStream = resource.getInputStream();
        JsonNode rootArray = objectMapper.readTree(inputStream);

        if (!rootArray.isArray()) {
            log.error("gcat_items.json root is not an array. Aborting seed.");
            return;
        }

        // Step 3: Seed questions + options
        int seeded = 0;
        int skipped = 0;
        List<String> errorCodes = new ArrayList<>();

        for (JsonNode node : rootArray) {
            try {
                String itemCode = node.path("item_code").asText(null);
                if (itemCode == null || itemCode.isBlank()) {
                    skipped++;
                    continue;
                }

                // Skip duplicates (idempotent)
                if (questionRepository.findByItemCode(itemCode).isPresent()) {
                    skipped++;
                    continue;
                }

                String subtestDimension = node.path("subtest_dimension").asText("ABSTRACT").toUpperCase();
                GcatSubtestCode subtestCode = GcatSubtestCode.from(subtestDimension);
                GcatSubtest subtest = subtestMap.get(subtestCode);

                if (subtest == null) {
                    log.warn("No subtest found for code: {}. Skipping item: {}", subtestCode, itemCode);
                    skipped++;
                    continue;
                }

                GcatDifficulty difficulty = GcatDifficulty.from(
                        node.path("difficulty").asText("MEDIUM").toUpperCase()
                );

                String examModeStr = node.path("exam_mode").asText("FULL").toUpperCase();
                ExamMode examMode = ExamMode.valueOf(examModeStr);

                GcatQuestion question = new GcatQuestion();
                question.setItemCode(itemCode);
                question.setSubtest(subtest);
                question.setTitleAr(node.path("title_in_arabic").asText(null));
                question.setPromptTextAr(node.path("prompt_text").asText(null));
                question.setPatternTypeAr(node.path("pattern_type").asText(null));
                question.setDifficulty(difficulty);
                question.setExamMode(examMode);
                question.setActive(true);
                question.setExposureCount(0);

                // Cognitive analysis fields
                JsonNode cogNode = node.path("cognitive_analysis");
                if (!cogNode.isMissingNode()) {
                    question.setObservationAr(cogNode.path("observation").asText(null));
                    question.setRuleAr(cogNode.path("rule").asText(null));
                    question.setApplicationAr(cogNode.path("application").asText(null));
                }

                // Correct answer key
                String correctKeyStr = node.path("correct_option_key").asText("A").toUpperCase();
                GcatOptionKey correctKey = GcatOptionKey.from(correctKeyStr);
                question.setCorrectOptionKey(correctKey);

                GcatQuestion savedQuestion = questionRepository.save(question);

                // Options: stored as object { "A": "text", "B": "text", ... }
                JsonNode optionsNode = node.path("options");
                if (optionsNode.isObject()) {
                    int order = 1;
                    for (GcatOptionKey key : GcatOptionKey.values()) {
                        String optText = optionsNode.path(key.name()).asText(null);
                        if (optText != null && !optText.isBlank()) {
                            GcatOption opt = new GcatOption(
                                    savedQuestion,
                                    key,
                                    optText,
                                    null,   // no image URL in JSON seed
                                    key == correctKey,
                                    order++
                            );
                            optionRepository.save(opt);
                        }
                    }
                }

                seeded++;

            } catch (Exception e) {
                String code = node.path("item_code").asText("UNKNOWN");
                log.error("Failed to seed GCAT item [{}]: {}", code, e.getMessage());
                errorCodes.add(code);
            }
        }

        log.info(">>> GCAT Seeding complete: {} seeded, {} skipped, {} errors.", seeded, skipped, errorCodes.size());
        if (!errorCodes.isEmpty()) {
            log.warn("Failed items: {}", errorCodes);
        }
    }

    private Map<GcatSubtestCode, GcatSubtest> seedSubtests() {
        Map<GcatSubtestCode, GcatSubtest> result = new java.util.EnumMap<>(GcatSubtestCode.class);

        for (Map.Entry<GcatSubtestCode, Object[]> entry : SUBTEST_META.entrySet()) {
            GcatSubtestCode code = entry.getKey();
            Object[] meta = entry.getValue();

            GcatSubtest subtest = subtestRepository.findByCode(code).orElseGet(() -> {
                GcatSubtest s = new GcatSubtest(
                        code,
                        (String) meta[0],
                        (String) meta[1],
                        (int) meta[2],
                        (int) meta[3],
                        (Integer) meta[4]
                );
                return subtestRepository.save(s);
            });
            result.put(code, subtest);
        }

        log.info("GCAT subtests ready: {}", result.keySet());
        return result;
    }
}
