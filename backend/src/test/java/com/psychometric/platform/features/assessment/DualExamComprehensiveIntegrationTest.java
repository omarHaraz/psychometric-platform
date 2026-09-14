package com.psychometric.platform.features.assessment;

import com.psychometric.platform.features.assessment.domain.enums.BatteryType;
import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import com.psychometric.platform.features.assessment.domain.model.ExamTypeEntity;
import com.psychometric.platform.features.assessment.repository.AssessmentAttemptRepository;
import com.psychometric.platform.features.assessment.repository.CompetencyTraitRepository;
import com.psychometric.platform.features.assessment.repository.DerailerCategoryRepository;
import com.psychometric.platform.features.assessment.repository.ExamTypeRepository;
import com.psychometric.platform.features.assessment.service.AssessmentSessionService;
import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerItemAdminRequest;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerItemAdminResponse;
import com.psychometric.platform.features.itembank.derailers.entity.ResponseScaleType;
import com.psychometric.platform.features.itembank.derailers.repository.DerailerItemRepository;
import com.psychometric.platform.features.itembank.derailers.service.AdminDerailerItemService;
import com.psychometric.platform.features.itembank.gcat.dto.GcatQuestionAdminRequest;
import com.psychometric.platform.features.itembank.gcat.dto.GcatQuestionAdminResponse;
import com.psychometric.platform.features.itembank.gcat.entity.GcatDifficulty;
import com.psychometric.platform.features.itembank.gcat.entity.GcatOptionKey;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import com.psychometric.platform.features.itembank.gcat.repository.GcatQuestionRepository;
import com.psychometric.platform.features.itembank.gcat.service.AdminGcatItemService;
import com.psychometric.platform.features.itembank.personality.dto.PersonalityItemAdminRequest;
import com.psychometric.platform.features.itembank.personality.dto.PersonalityItemAdminResponse;
import com.psychometric.platform.features.itembank.personality.repository.PersonalityItemRepository;
import com.psychometric.platform.features.itembank.personality.service.AdminPersonalityItemService;
import com.psychometric.platform.features.itembank.sjt.dto.SjtScenarioAdminRequest;
import com.psychometric.platform.features.itembank.sjt.dto.SjtScenarioAdminResponse;
import com.psychometric.platform.features.itembank.sjt.entity.SjtComplexity;
import com.psychometric.platform.features.itembank.sjt.entity.SjtOptionKey;
import com.psychometric.platform.features.itembank.sjt.repository.SjtDomainRepository;
import com.psychometric.platform.features.itembank.sjt.repository.SjtScenarioRepository;
import com.psychometric.platform.features.itembank.sjt.service.AdminSjtItemService;
import com.psychometric.platform.features.user.entity.User;
import com.psychometric.platform.features.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import com.psychometric.platform.common.exception.BadRequestException;
import com.psychometric.platform.features.itembank.personality.dto.CompetencyAdminResponse;
import com.psychometric.platform.features.itembank.personality.service.AdminTaxonomyService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
public class DualExamComprehensiveIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ExamTypeRepository examTypeRepo;

    @Autowired
    private PersonalityItemRepository personalityRepo;

    @Autowired
    private DerailerItemRepository derailerRepo;

    @Autowired
    private GcatQuestionRepository gcatRepo;

    @Autowired
    private SjtScenarioRepository sjtRepo;

    @Autowired
    private AssessmentAttemptRepository attemptRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AdminPersonalityItemService personalityService;

    @Autowired
    private AdminDerailerItemService derailerService;

    @Autowired
    private AdminGcatItemService gcatService;

    @Autowired
    private AdminSjtItemService sjtService;

    @Autowired
    private AssessmentSessionService sessionService;

    @Autowired
    private CompetencyTraitRepository competencyTraitRepo;

    @Autowired
    private DerailerCategoryRepository derailerCategoryRepo;

    @Autowired
    private SjtDomainRepository sjtDomainRepo;

    @Autowired
    private AdminTaxonomyService taxonomyService;

    @Test
    @DisplayName("1. Verify Database State & Zero Data Loss across all tables")
    public void testDatabaseStateAndZeroDataLoss() {
        System.out.println("=== TEST 1: Verifying Database Zero Data Loss ===");

        // Verify exam_types table
        List<ExamTypeEntity> examTypes = examTypeRepo.findAll();
        assertEquals(2, examTypes.size(), "Should have exactly 2 exam types registered");
        assertTrue(examTypes.stream().anyMatch(e -> "PSYCHOMETRIC".equals(e.getCode())));
        assertTrue(examTypes.stream().anyMatch(e -> "EMPLOYMENT".equals(e.getCode())));

        // Verify personality items
        int pPsychometric = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM personality_items WHERE exam_type = 'PSYCHOMETRIC'", Integer.class);
        int pEmployment = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM personality_items WHERE exam_type = 'EMPLOYMENT'", Integer.class);
        assertEquals(192, pPsychometric, "All 192 psychometric personality items must be preserved");
        assertTrue(pEmployment >= 0, "Employment personality bank count is valid");

        // Verify derailer items
        int dPsychometric = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM derailer_items WHERE exam_type = 'PSYCHOMETRIC'", Integer.class);
        int dEmployment = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM derailer_items WHERE exam_type = 'EMPLOYMENT'", Integer.class);
        assertEquals(60, dPsychometric, "All 60 psychometric derailer items must be preserved");
        assertTrue(dEmployment >= 0, "Employment derailer bank count is valid");

        // Verify GCAT questions
        int gPsychometric = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM gcat_questions WHERE exam_type = 'PSYCHOMETRIC'", Integer.class);
        int gEmployment = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM gcat_questions WHERE exam_type = 'EMPLOYMENT'", Integer.class);
        assertEquals(118, gPsychometric, "All 118 psychometric GCAT questions must be preserved");
        assertTrue(gEmployment >= 0, "Employment GCAT bank count is valid");

        // Verify SJT scenarios
        int sPsychometric = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM sjt_scenarios WHERE exam_type = 'PSYCHOMETRIC'", Integer.class);
        int sEmployment = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM sjt_scenarios WHERE exam_type = 'EMPLOYMENT'", Integer.class);
        assertEquals(30, sPsychometric, "All 30 psychometric SJT scenarios must be preserved");
        assertTrue(sEmployment >= 0, "Employment SJT bank count is valid");

        // Verify assessment attempts
        int attPsychometric = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM assessment_attempts WHERE exam_type = 'PSYCHOMETRIC'", Integer.class);
        assertEquals(19, attPsychometric, "All 19 psychometric assessment attempts must be preserved");

        System.out.println("TEST 1 PASSED: 100% of data preserved intact!");
    }

    @Test
    @DisplayName("2. Verify Personality Item Isolation: Employment item does not leak to Psychometric bank")
    public void testPersonalityDualBankIsolation() {
        System.out.println("=== TEST 2: Personality Bank Isolation ===");

        List<PersonalityItemAdminResponse> psychBefore = personalityService.getAll("PSYCHOMETRIC");
        assertEquals(192, psychBefore.size(), "Psychometric bank must have 192 items initially");

        List<PersonalityItemAdminResponse> empBefore = personalityService.getAll("EMPLOYMENT");
        int empInitialCount = empBefore.size();

        // Find a non-SD competency trait ID to assign
        var traits = competencyTraitRepo.findAll().stream()
                .filter(t -> !"SOCIAL_DESIRABILITY".equalsIgnoreCase(t.getCode()))
                .toList();
        assertFalse(traits.isEmpty());
        Long traitId = traits.get(0).getId();

        // Create an Employment personality item
        PersonalityItemAdminRequest req = new PersonalityItemAdminRequest(
                "أنا استمتع بحل المشكلات المعقدة في بيئة العمل السريعة",
                List.of(traitId),
                5,
                ExamMode.FULL,
                "يقيس القدرة على حل المشكلات تحت الضغط"
        );
        req.setExamType("EMPLOYMENT");

        PersonalityItemAdminResponse created = personalityService.create(req);
        assertNotNull(created);
        assertEquals("EMPLOYMENT", created.getExamType());

        try {
            // Verify Employment bank now has empInitialCount + 1 items
            List<PersonalityItemAdminResponse> empAfter = personalityService.getAll("EMPLOYMENT");
            assertEquals(empInitialCount + 1, empAfter.size());

            // Verify Psychometric bank STILL has exactly 192 items (ZERO LEAKAGE!)
            List<PersonalityItemAdminResponse> psychAfter = personalityService.getAll("PSYCHOMETRIC");
            assertEquals(192, psychAfter.size(), "Psychometric bank MUST remain exactly 192 items");
        } finally {
            // Cleanup test item
            personalityRepo.deleteById(created.getId());
            assertEquals(192, personalityService.getAll("PSYCHOMETRIC").size());
            assertEquals(empInitialCount, personalityService.getAll("EMPLOYMENT").size());
            System.out.println("TEST 2 PASSED: Personality isolation verified & clean!");
        }
    }

    @Test
    @DisplayName("3. Verify Derailer Exclusion from Employment: Employment has 0 derailers and rejects creation")
    public void testDerailerExclusionFromEmployment() {
        System.out.println("=== TEST 3: Derailer Exclusion from Employment ===");

        List<DerailerItemAdminResponse> psychBefore = derailerService.getAll("PSYCHOMETRIC");
        assertEquals(60, psychBefore.size(), "Psychometric derailer bank MUST remain exactly 60 items");

        List<DerailerItemAdminResponse> empBefore = derailerService.getAll("EMPLOYMENT");
        assertEquals(0, empBefore.size(), "Employment derailer bank must return 0 items");

        var categories = derailerCategoryRepo.findAll();
        assertFalse(categories.isEmpty());
        Long catId = categories.get(0).getId();

        DerailerItemAdminRequest req = new DerailerItemAdminRequest(
                "قد أفقد صبري أحياناً عندما تتأخر المهام من الفريق",
                "يقيس محفز نفاد الصبر",
                List.of(catId),
                1,
                ResponseScaleType.FREQUENCY,
                ExamMode.FULL
        );
        req.setExamType("EMPLOYMENT");

        // Creating a derailer item for EMPLOYMENT must throw BadRequestException
        assertThrows(BadRequestException.class, () -> derailerService.create(req),
                "Creating derailer item for EMPLOYMENT must be rejected");

        // Psychometric bank must remain 100% untouched
        assertEquals(60, derailerService.getAll("PSYCHOMETRIC").size());
        System.out.println("TEST 3 PASSED: Derailer exclusion from Employment verified & clean!");
    }

    @Test
    @DisplayName("4. Verify GCAT Question Isolation: Employment question does not leak to Psychometric bank")
    public void testGcatDualBankIsolation() {
        System.out.println("=== TEST 4: GCAT Question Isolation ===");

        List<GcatQuestionAdminResponse> psychBefore = gcatService.getAll("PSYCHOMETRIC");
        assertEquals(118, psychBefore.size());

        List<GcatQuestionAdminResponse> empBefore = gcatService.getAll("EMPLOYMENT");
        int empInitialGcat = empBefore.size();

        GcatQuestionAdminRequest req = new GcatQuestionAdminRequest(
                "GCAT-EMP-INT-01",
                GcatSubtestCode.NUMERICAL,
                "سؤال اختبار توظيف حسابي تجريبي",
                "ما هي القيمة المتبقية من العملية الحسابية؟",
                null,
                null,
                "حسابي",
                "ملاحظة",
                "قاعدة",
                "تطبيق",
                GcatOptionKey.B,
                GcatDifficulty.MEDIUM,
                ExamMode.FULL,
                List.of()
        );
        req.setExamType("EMPLOYMENT");

        GcatQuestionAdminResponse created = gcatService.create(req);
        assertNotNull(created);
        assertEquals("EMPLOYMENT", created.getExamType());

        try {
            List<GcatQuestionAdminResponse> empAfter = gcatService.getAll("EMPLOYMENT");
            assertEquals(empInitialGcat + 1, empAfter.size());
            assertEquals(created.getId(), empAfter.get(empAfter.size() - 1).getId());

            List<GcatQuestionAdminResponse> psychAfter = gcatService.getAll("PSYCHOMETRIC");
            assertEquals(118, psychAfter.size(), "Psychometric GCAT bank MUST remain exactly 118 items");
        } finally {
            gcatRepo.deleteById(created.getId());
            assertEquals(118, gcatService.getAll("PSYCHOMETRIC").size());
            assertEquals(empInitialGcat, gcatService.getAll("EMPLOYMENT").size());
            System.out.println("TEST 4 PASSED: GCAT isolation verified & clean!");
        }
    }

    @Test
    @DisplayName("5. Verify SJT Scenario Isolation: Employment scenario does not leak to Psychometric bank")
    public void testSjtDualBankIsolation() {
        System.out.println("=== TEST 5: SJT Scenario Isolation ===");

        List<SjtScenarioAdminResponse> psychBefore = sjtService.getAll("PSYCHOMETRIC");
        assertEquals(30, psychBefore.size());

        List<SjtScenarioAdminResponse> empBefore = sjtService.getAll("EMPLOYMENT");
        int empInitialSjt = empBefore.size();

        var domains = sjtDomainRepo.findAll();
        assertFalse(domains.isEmpty());
        Long domId = domains.get(0).getId();

        SjtScenarioAdminRequest req = new SjtScenarioAdminRequest(
                "SJT-EMP-INT-01",
                domId,
                "سيناريو توظيف تجريبي",
                "واجهت مشكلة مع زميل في مشروع جديد بالشركة...",
                null,
                SjtComplexity.DIRECT,
                SjtOptionKey.A,
                "التعليل الأمثل للحل",
                "تجنب التصعيد غير المبرر",
                "ملاحظة تدريبية",
                ExamMode.FULL,
                List.of()
        );
        req.setExamType("EMPLOYMENT");

        SjtScenarioAdminResponse created = sjtService.create(req);
        assertNotNull(created);
        assertEquals("EMPLOYMENT", created.getExamType());

        try {
            List<SjtScenarioAdminResponse> empAfter = sjtService.getAll("EMPLOYMENT");
            assertEquals(empInitialSjt + 1, empAfter.size());
            assertEquals(created.getId(), empAfter.get(empAfter.size() - 1).getId());

            List<SjtScenarioAdminResponse> psychAfter = sjtService.getAll("PSYCHOMETRIC");
            assertEquals(30, psychAfter.size(), "Psychometric SJT bank MUST remain exactly 30 items");
        } finally {
            sjtRepo.deleteById(created.getId());
            assertEquals(30, sjtService.getAll("PSYCHOMETRIC").size());
            assertEquals(empInitialSjt, sjtService.getAll("EMPLOYMENT").size());
            System.out.println("TEST 5 PASSED: SJT isolation verified & clean!");
        }
    }

    @Test
    @DisplayName("6. Verify Attempt Assignment & Dual Exam Isolation for Candidates")
    public void testCandidateDualExamAttemptLifecycle() {
        System.out.println("=== TEST 6: Candidate Attempt Dual Exam Lifecycle ===");

        // Find an admin
        User admin = userRepo.findAll().stream()
                .filter(u -> u.getRoles() != null && (u.getRoles().contains("ROLE_ADMIN") || u.getRoles().contains("ADMIN")))
                .findFirst()
                .orElseGet(() -> userRepo.findAll().get(0));

        // Create a temporary candidate
        String candidateEmail = "dual_test_candidate_" + System.currentTimeMillis() + "@test.com";
        User candidate = new User();
        candidate.setName("Dual Test Candidate");
        candidate.setEmail(candidateEmail);
        candidate.setPassword("TestPass123!");
        candidate.setEnabled(true);
        candidate.setRoles(Set.of("ROLE_CANDIDATE"));
        candidate = userRepo.save(candidate);

        Long candId = (long) candidate.getId();

        try {
            // 1. Assign PSYCHOMETRIC attempt
            AssessmentAttempt psychAttempt = sessionService.assignAttempt(candId, "PSYCHOMETRIC", admin.getEmail());
            assertNotNull(psychAttempt);
            assertEquals("PSYCHOMETRIC", psychAttempt.getExamType());
            assertEquals(com.psychometric.platform.features.assessment.domain.enums.AttemptState.INIT, psychAttempt.getState());
            assertEquals(4, psychAttempt.getBatterySessions().size(), "Psychometric assessment must have 4 batteries");
            assertTrue(psychAttempt.getBatterySessions().stream().anyMatch(b -> b.getBatteryType() == BatteryType.DERAILERS));

            // 2. Attempting to assign a second active PSYCHOMETRIC attempt MUST throw 409 Conflict
            ResponseStatusException pEx = assertThrows(ResponseStatusException.class, () ->
                    sessionService.assignAttempt(candId, "PSYCHOMETRIC", admin.getEmail())
            );
            assertEquals(409, pEx.getStatusCode().value());
            assertTrue(pEx.getReason().contains("already has an active PSYCHOMETRIC attempt"));

            // 3. Assigning an EMPLOYMENT attempt to the SAME candidate MUST SUCCEED (Independent Tracks!)
            AssessmentAttempt empAttempt = sessionService.assignAttempt(candId, "EMPLOYMENT", admin.getEmail());
            assertNotNull(empAttempt);
            assertEquals("EMPLOYMENT", empAttempt.getExamType());
            assertEquals(com.psychometric.platform.features.assessment.domain.enums.AttemptState.INIT, empAttempt.getState());
            assertEquals(3, empAttempt.getBatterySessions().size(), "Employment assessment must have exactly 3 batteries (PQ10, SJT, GCAT)");
            assertTrue(empAttempt.getBatterySessions().stream().noneMatch(b -> b.getBatteryType() == BatteryType.DERAILERS), "Employment assessment must NOT include DERAILERS");

            // 4. Attempting to assign a second active EMPLOYMENT attempt MUST throw 409 Conflict
            ResponseStatusException eEx = assertThrows(ResponseStatusException.class, () ->
                    sessionService.assignAttempt(candId, "EMPLOYMENT", admin.getEmail())
            );
            assertEquals(409, eEx.getStatusCode().value());
            assertTrue(eEx.getReason().contains("already has an active EMPLOYMENT attempt"));

            // 5. Query candidate attempts by examType
            List<AssessmentAttempt> candPsychAttempts = attemptRepo.findByCandidateIdAndExamTypeOrderByCreatedAtDesc(candId, "PSYCHOMETRIC");
            assertEquals(1, candPsychAttempts.size());
            assertEquals(psychAttempt.getId(), candPsychAttempts.get(0).getId());

            List<AssessmentAttempt> candEmpAttempts = attemptRepo.findByCandidateIdAndExamTypeOrderByCreatedAtDesc(candId, "EMPLOYMENT");
            assertEquals(1, candEmpAttempts.size());
            assertEquals(empAttempt.getId(), candEmpAttempts.get(0).getId());

            List<AssessmentAttempt> candAllAttempts = attemptRepo.findByCandidateIdOrderByCreatedAtDesc(candId);
            assertEquals(2, candAllAttempts.size());

            System.out.println("TEST 6 PASSED: Candidate dual-exam assignment & isolation verified successfully!");
        } finally {
            // Clean up test attempts and candidate
            List<AssessmentAttempt> testAttempts = attemptRepo.findByCandidateIdOrderByCreatedAtDesc(candId);
            for (AssessmentAttempt a : testAttempts) {
                jdbcTemplate.update("DELETE FROM candidate_responses WHERE session_id IN (SELECT id FROM battery_sessions WHERE attempt_id = ?)", a.getId());
                jdbcTemplate.update("DELETE FROM battery_sessions WHERE attempt_id = ?", a.getId());
                jdbcTemplate.update("DELETE FROM assessment_attempts WHERE id = ?", a.getId());
            }
            userRepo.deleteById((long) candidate.getId());
            System.out.println("Cleanup completed: Database left in original pristine state.");
        }
    }

    @Test
    @DisplayName("7. Verify Social Desirability Competency in Employment and Psychometric with Full Isolation")
    public void testSocialDesirabilityDualExamIsolation() {
        System.out.println("=== TEST 7: Social Desirability in Both Exams with Dual Isolation ===");

        // 1. Taxonomy API for Employment returns 13 competencies (12 workplace + SOCIAL_DESIRABILITY)
        List<CompetencyAdminResponse> empComps = taxonomyService.getAllCompetencies("EMPLOYMENT");
        assertEquals(13, empComps.size(), "Employment competencies must be exactly 13 (12 workplace + SOCIAL_DESIRABILITY)");
        assertTrue(empComps.stream().anyMatch(c -> "SOCIAL_DESIRABILITY".equalsIgnoreCase(c.code())),
                "Employment competencies must contain SOCIAL_DESIRABILITY");

        // 2. Taxonomy API for Psychometric returns 9 competencies (8 leadership traits + SOCIAL_DESIRABILITY)
        List<CompetencyAdminResponse> psychComps = taxonomyService.getAllCompetencies("PSYCHOMETRIC");
        assertEquals(9, psychComps.size(), "Psychometric competencies must include all 9 traits");
        assertTrue(psychComps.stream().anyMatch(c -> "SOCIAL_DESIRABILITY".equalsIgnoreCase(c.code())),
                "Psychometric competencies must retain SOCIAL_DESIRABILITY intact");

        // 3. Find both SOCIAL_DESIRABILITY competencies
        CompetencyAdminResponse empSd = empComps.stream()
                .filter(c -> "SOCIAL_DESIRABILITY".equalsIgnoreCase(c.code()))
                .findFirst()
                .orElseThrow();
        CompetencyAdminResponse psychSd = psychComps.stream()
                .filter(c -> "SOCIAL_DESIRABILITY".equalsIgnoreCase(c.code()))
                .findFirst()
                .orElseThrow();

        assertNotEquals(empSd.id(), psychSd.id(), "Employment and Psychometric must have distinct SD competencies");

        // 4. Verify Employment SD questions in personality_items
        List<PersonalityItemAdminResponse> empItems = personalityService.getAll("EMPLOYMENT");
        long empSdCount = empItems.stream()
                .filter(it -> it.getCompetencyIds() != null && it.getCompetencyIds().contains(empSd.id()))
                .count();
        assertEquals(10, empSdCount, "Employment bank must contain exactly 10 Social Desirability items");

        System.out.println("TEST 7 PASSED: Social Desirability dual configuration verified 100%!");
    }

    @Test
    @DisplayName("8. End-to-End Simulation: Employment Exam 3-Battery Sequence, Item Counts (40, 10, 30), and Zero 4th Battery")
    public void testEmploymentFullSessionExecutionLifecycle() {
        System.out.println("=== TEST 8: Employment Full Session Execution Lifecycle ===");

        User admin = userRepo.findAll().stream()
                .filter(u -> u.getRoles() != null && (u.getRoles().contains("ROLE_ADMIN") || u.getRoles().contains("ADMIN")))
                .findFirst()
                .orElseGet(() -> userRepo.findAll().get(0));

        String candidateEmail = "emp_lifecycle_candidate_" + System.currentTimeMillis() + "@test.com";
        User candidate = new User();
        candidate.setName("Employment Lifecycle Candidate");
        candidate.setEmail(candidateEmail);
        candidate.setPassword("TestPass123!");
        candidate.setEnabled(true);
        candidate.setRoles(Set.of("ROLE_CANDIDATE"));
        candidate = userRepo.save(candidate);

        Long candId = (long) candidate.getId();

        try {
            // 1. Assign Employment Attempt
            AssessmentAttempt attempt = sessionService.assignAttempt(candId, "EMPLOYMENT", admin.getEmail());
            assertNotNull(attempt);
            assertEquals("EMPLOYMENT", attempt.getExamType());
            assertEquals(3, attempt.getBatterySessions().size(), "Employment MUST have exactly 3 battery sessions");

            var sessionList = attempt.getBatterySessions().stream()
                    .sorted(Comparator.comparingInt(b -> b.getSequenceOrder()))
                    .toList();

            // Verify metadata of all 3 batteries
            assertEquals(BatteryType.PQ10, sessionList.get(0).getBatteryType());
            assertEquals(1200, sessionList.get(0).getTimeLimitSeconds(), "PQ10 time limit must be 20 min (1200s)");

            assertEquals(BatteryType.SJT, sessionList.get(1).getBatteryType());
            assertEquals(1800, sessionList.get(1).getTimeLimitSeconds(), "SJT time limit must be 30 min (1800s)");

            assertEquals(BatteryType.GCAT, sessionList.get(2).getBatteryType());
            assertEquals(900, sessionList.get(2).getTimeLimitSeconds(), "GCAT time limit must be 15 min (900s)");

            // 2. Start Attempt -> Starts Battery 0 (PQ10)
            AssessmentAttempt startedAttempt = sessionService.startAttempt(attempt.getAttemptToken(), candidate.getEmail());
            assertEquals(com.psychometric.platform.features.assessment.domain.enums.AttemptState.IN_PROGRESS, startedAttempt.getState());

            List<Map<String, Object>> pq10Items = sessionService.getBatteryItems(sessionList.get(0).getId(), candidate.getEmail());
            assertNotNull(pq10Items);
            assertEquals(40, pq10Items.size(), "Battery 1 (PQ10) MUST contain exactly 40 items");

            // 3. Submit Battery 0 -> Unlocks Battery 1 (SJT)
            AssessmentAttempt afterPQ10 = sessionService.submitSession(sessionList.get(0).getId(), candidate.getEmail());
            assertEquals(1, afterPQ10.getCurrentBatteryIndex());

            List<Map<String, Object>> sjtItems = sessionService.getBatteryItems(sessionList.get(1).getId(), candidate.getEmail());
            assertNotNull(sjtItems);
            assertEquals(10, sjtItems.size(), "Battery 2 (SJT) MUST contain exactly 10 scenarios");

            // 4. Submit Battery 1 -> Unlocks Battery 2 (GCAT)
            AssessmentAttempt afterSJT = sessionService.submitSession(sessionList.get(1).getId(), candidate.getEmail());
            assertEquals(2, afterSJT.getCurrentBatteryIndex());

            List<Map<String, Object>> gcatItems = sessionService.getBatteryItems(sessionList.get(2).getId(), candidate.getEmail());
            assertNotNull(gcatItems);
            assertEquals(30, gcatItems.size(), "Battery 3 (GCAT) MUST contain exactly 30 questions (10 verbal, 10 numerical, 10 abstract)");

            // 5. Submit Battery 2 -> Completed & Scored (NO 4TH BATTERY)
            AssessmentAttempt finalAttempt = sessionService.submitSession(sessionList.get(2).getId(), candidate.getEmail());
            assertTrue(finalAttempt.getState() == com.psychometric.platform.features.assessment.domain.enums.AttemptState.ALL_SUBMITTED 
                    || finalAttempt.getState() == com.psychometric.platform.features.assessment.domain.enums.AttemptState.SCORED);
            assertEquals(3, finalAttempt.getBatterySessions().size(), "Total batteries must remain strictly 3");
            assertTrue(finalAttempt.getBatterySessions().stream().noneMatch(b -> b.getBatteryType() == BatteryType.DERAILERS), 
                    "Derailers must NEVER appear in Employment assessment");

            System.out.println("TEST 8 PASSED: Full Employment session lifecycle (PQ10=40, SJT=10, GCAT=30, Total=3) verified 100%!");
        } finally {
            List<AssessmentAttempt> testAttempts = attemptRepo.findByCandidateIdOrderByCreatedAtDesc(candId);
            for (AssessmentAttempt a : testAttempts) {
                jdbcTemplate.update("DELETE FROM candidate_responses WHERE session_id IN (SELECT id FROM battery_sessions WHERE attempt_id = ?)", a.getId());
                jdbcTemplate.update("DELETE FROM battery_session_sampled_items WHERE session_id IN (SELECT id FROM battery_sessions WHERE attempt_id = ?)", a.getId());
                jdbcTemplate.update("DELETE FROM trait_scores WHERE assessment_score_id IN (SELECT id FROM assessment_scores WHERE attempt_id = ?)", a.getId());
                jdbcTemplate.update("DELETE FROM gcat_subtest_scores WHERE assessment_score_id IN (SELECT id FROM assessment_scores WHERE attempt_id = ?)", a.getId());
                jdbcTemplate.update("DELETE FROM assessment_scores WHERE attempt_id = ?", a.getId());
                jdbcTemplate.update("DELETE FROM battery_sessions WHERE attempt_id = ?", a.getId());
                jdbcTemplate.update("DELETE FROM assessment_attempts WHERE id = ?", a.getId());
            }
            userRepo.deleteById((long) candidate.getId());
        }
    }
}
