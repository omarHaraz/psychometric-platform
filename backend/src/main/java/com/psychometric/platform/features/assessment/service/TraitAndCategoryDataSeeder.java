package com.psychometric.platform.features.assessment.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.features.assessment.domain.model.CompetencyTrait;
import com.psychometric.platform.features.assessment.domain.model.DerailerCategory;
import com.psychometric.platform.features.assessment.repository.CompetencyTraitRepository;
import com.psychometric.platform.features.assessment.repository.DerailerCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class TraitAndCategoryDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TraitAndCategoryDataSeeder.class);

    private final CompetencyTraitRepository traitRepo;
    private final DerailerCategoryRepository categoryRepo;
    private final ObjectMapper objectMapper;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    public TraitAndCategoryDataSeeder(CompetencyTraitRepository traitRepo,
                                      DerailerCategoryRepository categoryRepo,
                                      ObjectMapper objectMapper,
                                      org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        this.traitRepo = traitRepo;
        this.categoryRepo = categoryRepo;
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        seedCompetencyTraits();
        seedDerailerCategories();
        seedSocialDesirabilityData();
    }

    private void seedSocialDesirabilityData() {
        try {
            List<Long> compIds = jdbcTemplate.queryForList("SELECT id FROM competencies WHERE code = 'SOCIAL_DESIRABILITY'", Long.class);
            Long sdCompId;
            if (compIds.isEmpty()) {
                jdbcTemplate.update(
                    "INSERT INTO competencies (code, name_ar, definition_ar, display_order) VALUES (?, ?, ?, ?)",
                    "SOCIAL_DESIRABILITY",
                    "التظاهر الاجتماعي",
                    "مقياس الصدق والتحقق من النزاهة والانطباع الاجتماعي",
                    9
                );
                sdCompId = jdbcTemplate.queryForObject("SELECT id FROM competencies WHERE code = 'SOCIAL_DESIRABILITY'", Long.class);
                log.info("Created SOCIAL_DESIRABILITY competency with ID: {}", sdCompId);
            } else {
                sdCompId = compIds.get(0);
            }

            List<String> sdStatements = List.of(
                "لم أكذب في حياتي إطلاقاً، حتى في أصغر الأمور.",
                "أفي بكل وعد أقطعه دون أي استثناء مهما تغيرت الظروف.",
                "لم أشعر بالغيرة تجاه أي شخص طوال حياتي.",
                "أستمع لكل من يتحدث إلي بصبر تام دون أن يتشتت ذهني ولو للحظة.",
                "لم يسبق أن أجّلت مهمة كان يجب علي إنجازها فوراً.",
                "أعترف بخطئي فور وقوعه دائماً، مهما كانت العواقب على سمعتي.",
                "لم أتحدث عن أحد في غيابه بشكل سلبي على الإطلاق.",
                "أعامل جميع من حولي بالتساوي التام دون أي استثناء أو تفضيل.",
                "لم أشك يوماً في صحة قرار اتخذته بعد اتخاذه.",
                "لا تخطر ببالي أفكار سلبية تجاه الآخرين مهما أساءوا إلي.",
                "لم يحدث أن نسبت لنفسي فضلاً يعود لشخص آخر، ولو بشكل غير مقصود.",
                "أحافظ على هدوئي التام في كل المواقف دون استثناء واحد."
            );

            int seeded = 0;
            for (String stmt : sdStatements) {
                List<Long> itemIds = jdbcTemplate.queryForList("SELECT id FROM personality_items WHERE statement_ar = ?", Long.class, stmt);
                Long itemId;
                if (itemIds.isEmpty()) {
                    jdbcTemplate.update(
                        "INSERT INTO personality_items (statement_ar, ideal_target, exam_mode, is_active, exposure_count, created_at, justification_ar) " +
                        "VALUES (?, 1, 'BOTH', 1, 0, NOW(), 'مقياس التظاهر الاجتماعي والصدق')",
                        stmt
                    );
                    itemId = jdbcTemplate.queryForObject("SELECT id FROM personality_items WHERE statement_ar = ? ORDER BY id DESC LIMIT 1", Long.class, stmt);
                    seeded++;
                } else {
                    itemId = itemIds.get(0);
                }

                Integer count = jdbcTemplate.queryForObject(
                    "SELECT count(*) FROM personality_item_competencies WHERE item_id = ? AND competency_id = ?",
                    Integer.class, itemId, sdCompId
                );
                if (count == null || count == 0) {
                    jdbcTemplate.update("INSERT INTO personality_item_competencies (item_id, competency_id) VALUES (?, ?)", itemId, sdCompId);
                }
            }

            log.info("Social desirability items verification complete ({} newly seeded, total 12 ensured).", seeded);
        } catch (Exception e) {
            log.error("Failed to seed social desirability items: {}", e.getMessage(), e);
        }
    }

    private void seedCompetencyTraits() {
        if (traitRepo.count() > 0) {
            log.info("Competency traits already seeded ({} found).", traitRepo.count());
            return;
        }

        try {
            ClassPathResource resource = new ClassPathResource("data/competencies.json");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    List<Map<String, Object>> list = objectMapper.readValue(is, new TypeReference<>() {});
                    for (Map<String, Object> map : list) {
                        CompetencyTrait trait = new CompetencyTrait();
                        trait.setCode((String) map.get("code"));
                        trait.setNameAr((String) map.get("nameAr"));
                        trait.setDefinitionAr((String) map.get("definitionAr"));
                        trait.setDisplayOrder(((Number) map.getOrDefault("displayOrder", 1)).intValue());
                        traitRepo.save(trait);
                    }
                    log.info("Successfully seeded {} competency traits from JSON.", list.size());
                    return;
                }
            }
        } catch (Exception e) {
            log.warn("Could not load data/competencies.json, falling back to hardcoded traits: {}", e.getMessage());
        }

        // Fallback hardcoded seed
        List<CompetencyTrait> defaults = List.of(
            new CompetencyTrait("COMMUNICATION_AND_INFLUENCE", "التواصل والتأثير الفعال", "القدرة على نقل المعلومات بوضوح وإقناع الآخرين من خلال الاستماع الفعال والتعبير الواضح عن الأفكار، مما يسهل تحقيق الأهداف المشتركة.", 1),
            new CompetencyTrait("INITIATIVE", "المبادرة", "القدرة على اتخاذ الخطوة الأولى دون انتظار توجيه، وتحمل مسؤولية إيجاد حلول للمشكلات واقتناص الفرص لتحسين الأداء.", 2),
            new CompetencyTrait("DECISION_MAKING_AND_RESPONSIBILITY", "اتخاذ القرار وتحمل المسؤولية", "القدرة على اتخاذ قرارات حاسمة في الوقت المناسب بناءً على المعلومات المتاحة، وتحمل نتائج هذه القرارات بشكل كامل.", 3),
            new CompetencyTrait("INSPIRING_LEADERSHIP", "القيادة الملهمة", "القدرة على تحفيز الآخرين وإلهامهم لتحقيق رؤية مشتركة، وبناء بيئة عمل إيجابية تشجع على التعاون والابتكار.", 4),
            new CompetencyTrait("STRATEGIC_THINKING", "التفكير الاستراتيجي", "القدرة على تحليل المعطيات واستشراف المستقبل لفهم الصورة الكبرى، ووضع خطط استراتيجية طويلة الأمد.", 5),
            new CompetencyTrait("SKILL_DEVELOPMENT", "تطوير المهارات", "القدرة على التعلم المستمر واكتساب مهارات جديدة، وتوجيه الآخرين وتشجيعهم على التطوير المهني والشخصي.", 6),
            new CompetencyTrait("ADAPTABILITY", "القدرة على التكيف", "القدرة على التعامل بمرونة مع المتغيرات والمواقف الجديدة، وتعديل الخطط بفعالية لضمان استمرارية العمل.", 7),
            new CompetencyTrait("SYSTEMATIC_ANALYSIS_AND_PLANNING", "التحليل والتخطيط المنهجي", "القدرة على دراسة المواقف وتحليل المشكلات بمنهجية منطقية، ووضع خطط دقيقة ومنظمة لتحقيق الأهداف بكفاءة.", 8)
        );
        traitRepo.saveAll(defaults);
        log.info("Successfully seeded 8 default competency traits.");
    }

    private void seedDerailerCategories() {
        if (categoryRepo.count() > 0) {
            log.info("Derailer categories already seeded ({} found).", categoryRepo.count());
            return;
        }

        try {
            ClassPathResource resource = new ClassPathResource("data/derailer_types.json");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    List<Map<String, Object>> list = objectMapper.readValue(is, new TypeReference<>() {});
                    int order = 1;
                    for (Map<String, Object> map : list) {
                        DerailerCategory cat = new DerailerCategory();
                        cat.setNameAr((String) map.get("nameAr"));
                        cat.setDefinitionAr((String) map.get("definitionAr"));
                        @SuppressWarnings("unchecked")
                        List<String> indicators = (List<String>) map.get("indicators");
                        cat.setIndicatorsAr(indicators);
                        cat.setDisplayOrder(order++);
                        categoryRepo.save(cat);
                    }
                    log.info("Successfully seeded {} derailer categories from JSON.", list.size());
                    return;
                }
            }
        } catch (Exception e) {
            log.warn("Could not load data/derailer_types.json, falling back to hardcoded categories: {}", e.getMessage());
        }

        // Fallback hardcoded seed
        List<DerailerCategory> defaults = List.of(
            new DerailerCategory("التحفظ", "ميل إلى الانسحاب والعزلة عن الآخرين.", List.of("الظهور بمظهر غير ودود أو غير مهتم بالآخرين.", "الظهور بمزاج مكتئب أو حزين.", "تجنب الآخرين والحفاظ على مسافة في العلاقات."), 1),
            new DerailerCategory("الانفعالية", "ميل إلى التركيز على العيوب وإظهار المشاعر السلبية.", List.of("امتلاك نظرة سلبية وانخفاض تقدير الذات.", "التقلب المزاجي أو القلق.", "الظهور بمظهر المتشكك أو المتحفظ تجاه الآخرين."), 2),
            new DerailerCategory("العدائية", "ميل إلى العدوانية في التعامل مع الآخرين واعتماد أسلوب مباشر وصدامي في التواصل.", List.of("إظهار العداء والعدوانية تجاه الآخرين.", "السعي لتحقيق الأهداف بأي ثمن والتعامل مع الآخرين بدافع هذا السعي.", "التركيز المفرط على الذات وقلة الاهتمام بآراء ومشاعر الآخرين."), 3),
            new DerailerCategory("الاندفاعية", "ميل إلى الاندفاع والمخاطرة دون تفكير كافٍ.", List.of("قلة التركيز والانتباه لفترات طويلة.", "التهاون في الالتزامات وعدم متابعة المهام حتى النهاية.", "التصرف بطيش واتخاذ قرارات متسرعة وغير مدروسة."), 4),
            new DerailerCategory("الصرامة", "ميل إلى الصرامة وعدم المرونة وعدم التسامح مع ما يعتبر تقصيراً في الالتزامات.", List.of("التعامل بصرامة وجمود في المواقف.", "وضع أهداف غير واقعية لأنفسهم وللآخرين.", "عدم التسامح مع الأخطاء واعتبار أي تقصير فشلاً."), 5),
            new DerailerCategory("اللامألوفية", "ميل إلى إظهار سلوكيات غير مألوفة وتجاهل الأعراف الاجتماعية والتعبير عن أفكار أو معتقدات غير تقليدية.", List.of("امتلاك أساليب تفكير غير اعتيادية ومعتقدات غير مألوفة.", "الظهور بمظهر غريب أو مختلف في نظر الآخرين.", "العجز عن تفسير كيفية أو أسباب أفعالهم."), 6)
        );
        categoryRepo.saveAll(defaults);
        log.info("Successfully seeded 6 default derailer categories.");
    }
}
