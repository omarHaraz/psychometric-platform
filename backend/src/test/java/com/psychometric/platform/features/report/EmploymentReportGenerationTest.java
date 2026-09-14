package com.psychometric.platform.features.report;

import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import com.psychometric.platform.features.assessment.domain.enums.ReadinessBand;
import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import com.psychometric.platform.features.assessment.domain.model.AssessmentScore;
import com.psychometric.platform.features.assessment.domain.model.CompetencyTrait;
import com.psychometric.platform.features.assessment.domain.model.GcatSubtestScore;
import com.psychometric.platform.features.assessment.domain.model.TraitScore;
import com.psychometric.platform.features.user.entity.User;
import com.psychometric.platform.features.report.dto.EmploymentReportDto;
import com.psychometric.platform.features.report.service.EmploymentReportGeneratorService;
import com.psychometric.platform.features.report.service.PdfGeneratorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class EmploymentReportGenerationTest {

    @Autowired
    private EmploymentReportGeneratorService generatorService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    private AssessmentScore createMockScore(double pq, double sjt, double verbal, double num, double abs) {
        User candidate = new User();
        candidate.setName("سارة أحمد المنصوري");

        AssessmentAttempt attempt = new AssessmentAttempt();
        attempt.setCandidate(candidate);
        attempt.setExamType("EMPLOYMENT");
        attempt.setAttemptToken("EMP-TEST-TOKEN-12345");

        double gcat = (0.30 * verbal) + (0.30 * num) + (0.40 * abs);
        double composite = (0.30 * pq) + (0.30 * sjt) + (0.40 * gcat);

        AssessmentScore score = new AssessmentScore();
        score.setAttempt(attempt);
        score.setPersonalityScorePct(pq);
        score.setSjtScorePct(sjt);
        score.setCognitiveScorePct(Math.round(gcat * 100.0) / 100.0);
        score.setCompositeScore(Math.round(composite * 100.0) / 100.0);
        score.setScoredAt(Instant.now());

        Set<GcatSubtestScore> gcatScores = new HashSet<>();
        gcatScores.add(new GcatSubtestScore(score, GcatSubtestCode.VERBAL, (int)(verbal / 10.0), verbal));
        gcatScores.add(new GcatSubtestScore(score, GcatSubtestCode.NUMERICAL, (int)(num / 10.0), num));
        gcatScores.add(new GcatSubtestScore(score, GcatSubtestCode.ABSTRACT, (int)(abs / 10.0), abs));
        score.setGcatSubtestScores(gcatScores);

        Set<TraitScore> traits = new HashSet<>();
        String[] compNames = {
            "الانضباط والمسؤولية", "التعاون والعمل الجماعي", "الاستقرار الانفعالي",
            "الانفتاح على التجربة", "التفاعل والتواصل", "المرونة والتكيف",
            "الدافعية والمبادرة", "الثقة بالنفس", "الرشاقة الرقمية",
            "التفكير الناقد", "النزاهة وأخلاقيات العمل", "التوجه نحو الخدمة"
        };
        for (int i = 0; i < compNames.length; i++) {
            CompetencyTrait trait = new CompetencyTrait("EMP_COMP_" + (i + 1), compNames[i], "تعريف", i + 1, "EMPLOYMENT");
            traits.add(new TraitScore(score, trait, pq * 0.68, pq));
        }
        score.setTraitScores(traits);

        return score;
    }

    @Test
    @DisplayName("1. Case 1: Excellent (>=85%) triggers integrated exceptional performance case and generates PDF")
    public void testCase1ExcellentReportGeneration() {
        AssessmentScore score = createMockScore(88.0, 90.0, 85.0, 90.0, 92.0);
        EmploymentReportDto dto = generatorService.generateReport(score);

        assertNotNull(dto);
        assertEquals(1, dto.getCaseNumber());
        assertEquals(ReadinessBand.EXCELLENT, dto.getReadinessBand());
        assertEquals("ممتاز", dto.getReadinessBandLabelAr());
        assertTrue(dto.getDiagnosticCaseTitle().contains("تكامل الأداء والجدارة الاستثنائية"));
        assertTrue(dto.getActionPlanTitle().contains("خطة الاستثمار المهني والانطلاق"));
        assertEquals(12, dto.getCompetencyScores().size());

        // Verify PDF Generation
        byte[] pdfBytes = pdfGeneratorService.generateEmploymentPdfReport(dto, "ar");
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 5000, "PDF bytes should be substantial");
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get("target/employment-report-case1-excellent.pdf"), pdfBytes);
        } catch (Exception e) {}
        System.out.println("Case 1 PDF generated successfully! Size: " + pdfBytes.length + " bytes.");
    }

    @Test
    @DisplayName("2. Case 2: Very Good (75-84.9%) triggers targeted empowerment case with dynamic lowest part and generates PDF")
    public void testCase2VeryGoodReportGeneration() {
        // PQ = 82%, SJT = 85%, Verbal = 65%, Num = 70%, Abs = 75% -> GCAT ~ 70.5%
        AssessmentScore score = createMockScore(82.0, 85.0, 65.0, 70.0, 75.0);
        EmploymentReportDto dto = generatorService.generateReport(score);

        assertNotNull(dto);
        assertEquals(2, dto.getCaseNumber());
        assertEquals(ReadinessBand.STRONG, dto.getReadinessBand());
        assertEquals("جيد جداً", dto.getReadinessBandLabelAr());
        assertTrue(dto.getDiagnosticCaseTitle().contains("جاهزية مهنية متقدمة مع تفاوت طفيف"));
        assertTrue(dto.getActionPlanTitle().contains("خطة التمكين والارتقاء الموجهة"));
        assertNotNull(dto.getHighestComponentName());
        assertNotNull(dto.getLowestComponentName());

        byte[] pdfBytes = pdfGeneratorService.generateEmploymentPdfReport(dto, "ar");
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 5000);
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get("target/employment-report-case2-very-good.pdf"), pdfBytes);
        } catch (Exception e) {}
        System.out.println("Case 2 PDF generated successfully! Size: " + pdfBytes.length + " bytes.");
    }

    @Test
    @DisplayName("3. Case 3: Good (60-74.9%) triggers remedial action plan for the weak component and generates PDF")
    public void testCase3GoodReportGeneration() {
        // PQ = 70%, SJT = 75%, GCAT weak: Verbal = 50%, Num = 50%, Abs = 55% -> GCAT ~ 52%
        AssessmentScore score = createMockScore(70.0, 75.0, 50.0, 50.0, 55.0);
        EmploymentReportDto dto = generatorService.generateReport(score);

        assertNotNull(dto);
        assertEquals(3, dto.getCaseNumber());
        assertEquals(ReadinessBand.ACCEPTABLE, dto.getReadinessBand());
        assertEquals("جيد", dto.getReadinessBandLabelAr());
        assertTrue(dto.getDiagnosticCaseTitle().contains("جاهزية أساسية مقيدة بفجوة في أحد الأجزاء"));
        assertTrue(dto.getActionPlanTitle().contains("خطة التطوير العلاجية"));

        byte[] pdfBytes = pdfGeneratorService.generateEmploymentPdfReport(dto, "ar");
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 5000);
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get("target/employment-report-case3-good.pdf"), pdfBytes);
        } catch (Exception e) {}
        System.out.println("Case 3 PDF generated successfully! Size: " + pdfBytes.length + " bytes.");
    }

    @Test
    @DisplayName("4. Case 4: Weak (<60% or two <50%) triggers comprehensive re-skilling path and generates PDF")
    public void testCase4WeakReportGeneration() {
        AssessmentScore score = createMockScore(48.0, 52.0, 45.0, 40.0, 50.0);
        EmploymentReportDto dto = generatorService.generateReport(score);

        assertNotNull(dto);
        assertEquals(4, dto.getCaseNumber());
        assertEquals(ReadinessBand.FOUNDATIONAL, dto.getReadinessBand());
        assertEquals("ضعيف", dto.getReadinessBandLabelAr());
        assertTrue(dto.getDiagnosticCaseTitle().contains("حاجة ماسة لإعادة التأهيل المعرفي والسلوكي"));
        assertTrue(dto.getActionPlanTitle().contains("خطة إعادة التأهيل الشاملة"));

        byte[] pdfBytes = pdfGeneratorService.generateEmploymentPdfReport(dto, "ar");
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 5000);
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get("target/employment-report-case4-weak.pdf"), pdfBytes);
        } catch (Exception e) {}
        System.out.println("Case 4 PDF generated successfully! Size: " + pdfBytes.length + " bytes.");
    }
}
