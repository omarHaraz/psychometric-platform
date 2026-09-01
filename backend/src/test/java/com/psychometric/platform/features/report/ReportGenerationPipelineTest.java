package com.psychometric.platform.features.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.features.assessment.domain.enums.ReadinessBand;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto.DerailerCategoryScoreDto;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto.GcatSubtestScoreDto;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto.TraitScoreDto;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import com.psychometric.platform.features.report.dto.ReportContextDto;
import com.psychometric.platform.features.report.service.GeminiAiReportClient;
import com.psychometric.platform.features.report.service.LeadershipReportGeneratorService;
import com.psychometric.platform.features.report.service.PdfGeneratorService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
public class ReportGenerationPipelineTest {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testEndToEndReportGenerationAndSavePdf() throws Exception {
        System.out.println("==================================================");
        System.out.println("1. Initializing AssessmentScoreResponseDto test data...");
        System.out.println("==================================================");

        AssessmentScoreResponseDto rawScore = new AssessmentScoreResponseDto();
        rawScore.setId(1001L);
        rawScore.setAttemptToken("PCIV126371");
        rawScore.setCandidateName("سعد ناصر العتيبي");
        rawScore.setCandidateEmail("saad.alotaibi@example.com");
        rawScore.setCompositeScore(88.5);
        rawScore.setPersonalityScorePct(82.0);
        rawScore.setSjtScorePct(78.0);
        rawScore.setDerailersEffectiveScorePct(90.0);
        rawScore.setCognitiveScorePct(84.0);
        rawScore.setPercentile(92);
        rawScore.setReadinessBand(ReadinessBand.EXCELLENT);
        rawScore.setSocialDesirabilityRiskPct(30.0);
        rawScore.setCentralTendencyRatePct(20.0);

        // Add 8 Competency Traits
        rawScore.getTraitScores().addAll(List.of(
                new TraitScoreDto("COMMUNICATION_AND_INFLUENCE", "التواصل والتأثير الفعال", 1, 62.0, 91.0),
                new TraitScoreDto("INITIATIVE", "المبادرة", 2, 45.0, 66.0),
                new TraitScoreDto("DECISION_MAKING_AND_RESPONSIBILITY", "اتخاذ القرار وتحمل المسؤولية", 3, 54.0, 79.0),
                new TraitScoreDto("INSPIRING_LEADERSHIP", "القيادة الملهمة", 4, 52.0, 76.0),
                new TraitScoreDto("STRATEGIC_THINKING", "التفكير الاستراتيجي", 5, 32.0, 47.0),
                new TraitScoreDto("SKILL_DEVELOPMENT", "تطوير المهارات", 6, 35.0, 51.0),
                new TraitScoreDto("ADAPTABILITY", "القدرة على التكيف", 7, 56.0, 82.0),
                new TraitScoreDto("SYSTEMATIC_ANALYSIS_AND_PLANNING", "التحليل والتخطيط المنهجي", 8, 64.0, 94.0)
        ));

        // Add 6 Derailers
        rawScore.getDerailerCategoryScores().addAll(List.of(
                new DerailerCategoryScoreDto(1L, "التحفظ", 1, 12.0, 60.0),
                new DerailerCategoryScoreDto(2L, "الانفعالية", 2, 10.0, 50.0),
                new DerailerCategoryScoreDto(3L, "العدائية", 3, 12.0, 60.0),
                new DerailerCategoryScoreDto(4L, "الاندفاعية", 4, 12.0, 60.0),
                new DerailerCategoryScoreDto(5L, "الصرامة", 5, 12.0, 60.0),
                new DerailerCategoryScoreDto(6L, "اللامألوفية", 6, 14.0, 70.0)
        ));

        // Add 3 GCAT Subtests
        rawScore.getGcatSubtestScores().addAll(List.of(
                new GcatSubtestScoreDto(GcatSubtestCode.ABSTRACT, 11, 14, 78.5),
                new GcatSubtestScoreDto(GcatSubtestCode.NUMERICAL, 12, 14, 85.7),
                new GcatSubtestScoreDto(GcatSubtestCode.VERBAL, 7, 14, 50.0)
        ));

        System.out.println("==================================================");
        System.out.println("2. Testing Gemini AI API Client & Narrative Generator...");
        System.out.println("==================================================");

        // Instantiate Gemini client
        GeminiAiReportClient geminiClient = new GeminiAiReportClient(
                WebClient.builder(),
                objectMapper,
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent",
                "AQ.Ab8RN6IfhJmRgsHl07KaoxZ0hMKJULn6TWq4M485UTGeygdDhg"
        );

        LeadershipReportGeneratorService generatorService = new LeadershipReportGeneratorService(
                objectMapper,
                Optional.of(geminiClient)
        );

        ReportContextDto reportDto = generatorService.generateReport(rawScore);

        assertNotNull(reportDto, "ReportContextDto should not be null");
        assertEquals("PCIV126371", reportDto.getCandidateId());
        assertEquals("88.5", reportDto.getResultScore());
        assertNotNull(reportDto.getSocialInterpretation(), "socialInterpretation should be populated");
        assertNotNull(reportDto.getReservedText(), "reservedText should be populated");
        assertNotNull(reportDto.getGrowGoalText(), "growGoalText should be populated");

        System.out.println("Candidate ID: " + reportDto.getCandidateId());
        System.out.println("Social Interpretation: " + reportDto.getSocialInterpretation());
        System.out.println("Reserved Text: " + reportDto.getReservedText());
        System.out.println("GROW Goal Text: " + reportDto.getGrowGoalText());
        System.out.println("Competency Pages Generated: " + reportDto.getCompetencyPages().size());

        System.out.println("==================================================");
        System.out.println("3. Testing PDF Generation Service (OpenHTMLtoPDF + Master Template)...");
        System.out.println("==================================================");

        PdfGeneratorService pdfGeneratorService = new PdfGeneratorService(templateEngine);
        byte[] pdfBytes = pdfGeneratorService.generatePdfReport(reportDto);

        assertNotNull(pdfBytes, "Generated PDF byte array should not be null");
        assertTrue(pdfBytes.length > 10000, "PDF size should be substantial (actual: " + pdfBytes.length + " bytes)");

        // Save PDF file to project root
        File outputFile = new File("test_leadership_report_15_pages.pdf");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(pdfBytes);
        }

        System.out.println("Successfully saved test PDF to: " + outputFile.getAbsolutePath());
        System.out.println("File size: " + (outputFile.length() / 1024) + " KB");

        System.out.println("==================================================");
        System.out.println("4. Verifying PDF with Apache PDFBox...");
        System.out.println("==================================================");

        try (PDDocument document = PDDocument.load(outputFile)) {
            int pageCount = document.getNumberOfPages();
            System.out.println("Total PDF Pages Rendered: " + pageCount);
            assertTrue(pageCount >= 15, "Expected at least 15 pages in full report, found: " + pageCount);
        }

        System.out.println("==================================================");
        System.out.println("TEST PASSED: 15-Page Report successfully generated and verified!");
        System.out.println("==================================================");
    }

    @Test
    public void testModularCandidateContextProfiles() {
        GeminiAiReportClient client = new GeminiAiReportClient(
                WebClient.builder(),
                objectMapper,
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent",
                ""
        );

        // 1. Impression Management
        var impPayload = new LeadershipReportGeneratorService.ImpressionPayload(8, "مرتفع", 1, "منخفض");
        var impResp = client.generateImpressionNarratives(impPayload);
        assertNotNull(impResp);
        assertNotNull(impResp.socialInterpretation);
        assertNotNull(impResp.centralInterpretation);

        // 2. Derailers
        var derPayload = new LeadershipReportGeneratorService.DerailersPayload(7, 4, 3, 8, 5, 6);
        var derResp = client.generateDerailersNarratives(derPayload);
        assertNotNull(derResp);
        assertNotNull(derResp.reservedText);
        assertNotNull(derResp.impulsivityText);

        // 3. Competency Page 9
        var compPayload = new LeadershipReportGeneratorService.CompetencyPagePayload(
                9,
                "اتخاذ القرار وتحمل المسؤولية",
                3.53,
                "السمات الشخصية الأبرز للمرشح: الاندفاعية (8/10) والتحفظ (7/10)",
                List.of(
                        "يُظهر ثقة كبيرة في اتخاذ القرارات وتحمل النتائج بشجاعة.",
                        "يمتلك قدرة معرفية متقدمة لتحليل المعلومات واتخاذ قرارات مسؤولة.",
                        "يتسم بالدقة والانضباط عند اتخاذ القرارات وتحمل المسؤولية بشجاعة."
                ),
                """
                السؤال 1: [أعتمد على التحليل المنطقي والبيانات الموثوقة عند المفاضلة بين الخيارات] - إجابة المرشح: [أوافق / 4]
                السؤال 2: [أحسم القرارات الصعبة في الأوقات الحرجة دون تردد مفرط] - إجابة المرشح: [محايد / 3]
                السؤال 3: [أتحمل المسؤولية الكاملة عن تبعات ونتائج القرارات التي أتخذها] - إجابة المرشح: [أوافق بشدة / 5]
                """
        );
        var compResp = client.generateCompetencyPageNarratives(compPayload);
        assertNotNull(compResp);
        assertNotNull(compResp.req1);
        assertNotNull(compResp.result1);
        assertNotNull(compResp.rec1);

        // 4. GROW Plan
        var growPayload = new LeadershipReportGeneratorService.GrowPlanPayload(
                "سعد ناصر العتيبي",
                "التحليل والتخطيط المنهجي (4.0/5)، التواصل والتأثير الفعال (4.0/5)",
                "التفكير الاستراتيجي (2.0/5)، المبادرة (2.0/5)",
                "السمات الشخصية الأبرز: الاندفاعية (8/10)"
        );
        var growResp = client.generateGrowPlanNarratives(growPayload);
        assertNotNull(growResp);
        assertNotNull(growResp.growGoalText);
        assertNotNull(growResp.growRealityText);
    }

    @Test
    public void testPage2And4DedicatedNarratives() {
        AssessmentScoreResponseDto rawScore = new AssessmentScoreResponseDto();
        rawScore.setAttemptToken("PCIV-P2P4-TEST");
        rawScore.setSocialDesirabilityRiskPct(75.0);
        rawScore.setCentralTendencyRatePct(15.0);

        rawScore.setDerailerCategoryScores(List.of(
                new AssessmentScoreResponseDto.DerailerCategoryScoreDto(1L, "التحفظ", 1, 7.0, 70.0),
                new AssessmentScoreResponseDto.DerailerCategoryScoreDto(2L, "الانفعالية", 2, 4.0, 40.0),
                new AssessmentScoreResponseDto.DerailerCategoryScoreDto(3L, "العدائية", 3, 3.0, 30.0),
                new AssessmentScoreResponseDto.DerailerCategoryScoreDto(4L, "الاندفاعية", 4, 8.0, 80.0),
                new AssessmentScoreResponseDto.DerailerCategoryScoreDto(5L, "الصرامة", 5, 5.0, 50.0),
                new AssessmentScoreResponseDto.DerailerCategoryScoreDto(6L, "اللامألوفية", 6, 6.0, 60.0)
        ));

        LeadershipReportGeneratorService generatorService = new LeadershipReportGeneratorService(
                objectMapper,
                Optional.empty()
        );

        // Step 1: Wire Up Page 2
        var impResp = generatorService.generateImpressionManagementNarratives("PCIV-P2P4-TEST", rawScore);
        assertNotNull(impResp, "Impression response should not be null");
        assertNotNull(impResp.socialInterpretation, "socialInterpretation should not be null");
        assertNotNull(impResp.centralInterpretation, "centralInterpretation should not be null");

        // Step 2: Wire Up Page 4
        var derResp = generatorService.generateDerailersNarratives("PCIV-P2P4-TEST", rawScore);
        assertNotNull(derResp, "Derailers response should not be null");
        assertNotNull(derResp.reservedText, "reservedText should not be null");
        assertNotNull(derResp.impulsivityText, "impulsivityText should not be null");
        assertNotNull(derResp.unconventionalityText, "unconventionalityText should not be null");
    }

    @Test
    public void testReportCaching() {
        AssessmentScoreResponseDto rawScore = new AssessmentScoreResponseDto();
        rawScore.setAttemptToken("PCIV-CACHE-TEST");
        rawScore.setCandidateName("عبدالله محمد السالم");
        rawScore.setCompositeScore(85.0);

        LeadershipReportGeneratorService generatorService = new LeadershipReportGeneratorService(
                objectMapper,
                Optional.empty()
        );

        ReportContextDto report1 = generatorService.generateReport(rawScore);
        ReportContextDto report2 = generatorService.generateReport(rawScore);

        assertNotNull(report1);
        assertNotNull(report2);
        assertEquals(report1.getCandidateId(), report2.getCandidateId());
        assertEquals(report1.getResultScore(), report2.getResultScore());
    }

    @Test
    public void testForceRefreshCacheEviction() {
        LeadershipReportGeneratorService generatorService = new LeadershipReportGeneratorService(
                objectMapper,
                Optional.empty()
        );

        // Verify clearCandidateCaches runs safely without exceptions even without full CacheManager
        assertDoesNotThrow(() -> generatorService.clearCandidateCaches("PCIV-FORCE-REFRESH-TEST"));
    }
}
