package com.psychometric.platform.features.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.features.assessment.domain.enums.ReadinessBand;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto.DerailerCategoryScoreDto;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto.GcatSubtestScoreDto;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto.TraitScoreDto;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import com.psychometric.platform.features.report.dto.ReportContextDto;
import com.psychometric.platform.features.report.service.LeadershipReportGeneratorService;
import com.psychometric.platform.features.report.service.PdfGeneratorService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
public class BilingualPdfGenerationTest {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private ObjectMapper objectMapper;

    private AssessmentScoreResponseDto createSampleScoreDto() {
        AssessmentScoreResponseDto rawScore = new AssessmentScoreResponseDto();
        rawScore.setId(1002L);
        rawScore.setAttemptToken("BILINGUAL-TEST-TOKEN");
        rawScore.setCandidateName("Ahmed Al-Mansouri");
        rawScore.setCandidateEmail("ahmed.mansouri@example.com");
        rawScore.setCompositeScore(85.0);
        rawScore.setPersonalityScorePct(80.0);
        rawScore.setSjtScorePct(75.0);
        rawScore.setDerailersEffectiveScorePct(88.0);
        rawScore.setCognitiveScorePct(82.0);
        rawScore.setPercentile(90);
        rawScore.setReadinessBand(ReadinessBand.EXCELLENT);
        rawScore.setSocialDesirabilityRiskPct(25.0);
        rawScore.setCentralTendencyRatePct(15.0);

        rawScore.getTraitScores().addAll(List.of(
                new TraitScoreDto("COMMUNICATION_AND_INFLUENCE", "التواصل والتأثير الفعال", 1, 62.0, 91.0),
                new TraitScoreDto("INITIATIVE", "المبادرة", 2, 45.0, 66.0),
                new TraitScoreDto("DECISION_MAKING_AND_RESPONSIBILITY", "اتخاذ القرار وتحمل المسؤولية", 3, 54.0, 79.0),
                new TraitScoreDto("INSPIRING_LEADERSHIP", "القيادة الملهمة", 4, 52.0, 76.0),
                new TraitScoreDto("STRATEGIC_THINKING", "التفكير الاستراتيجي", 5, 58.0, 85.0),
                new TraitScoreDto("SKILL_DEVELOPMENT", "تطوير المهارات", 6, 42.0, 61.0),
                new TraitScoreDto("ADAPTABILITY", "القدرة على التكيف", 7, 50.0, 73.0),
                new TraitScoreDto("SYSTEMATIC_ANALYSIS_AND_PLANNING", "التحليل والتخطيط المنهجي", 8, 48.0, 70.0)
        ));

        rawScore.getDerailerCategoryScores().addAll(List.of(
                new DerailerCategoryScoreDto(1L, "التحفظ", 1, 35.0, 87.5),
                new DerailerCategoryScoreDto(2L, "الانفعالية", 2, 28.0, 70.0),
                new DerailerCategoryScoreDto(3L, "العدائية", 3, 38.0, 95.0),
                new DerailerCategoryScoreDto(4L, "الاندفاعية", 4, 30.0, 75.0),
                new DerailerCategoryScoreDto(5L, "الصرامة", 5, 32.0, 80.0),
                new DerailerCategoryScoreDto(6L, "اللامألوفية", 6, 36.0, 90.0)
        ));

        rawScore.getGcatSubtestScores().addAll(List.of(
                new GcatSubtestScoreDto(GcatSubtestCode.ABSTRACT, 16, 20, 80.0),
                new GcatSubtestScoreDto(GcatSubtestCode.NUMERICAL, 17, 20, 85.0),
                new GcatSubtestScoreDto(GcatSubtestCode.VERBAL, 15, 20, 75.0)
        ));

        return rawScore;
    }

    @Test
    @DisplayName("Test Arabic (RTL) PDF Generation")
    public void testArabicRtlPdfGeneration() throws Exception {
        LeadershipReportGeneratorService genService = new LeadershipReportGeneratorService(objectMapper);
        PdfGeneratorService pdfService = new PdfGeneratorService(templateEngine);

        ReportContextDto reportContext = genService.generateReport(createSampleScoreDto());
        assertNotNull(reportContext);

        // Generate Arabic HTML & check dir / slider
        String html = pdfService.generateHtmlReport(reportContext, "ar");
        assertTrue(html.contains("dir=\"rtl\""), "Arabic HTML must have dir='rtl'");
        assertTrue(html.contains("right:"), "Arabic HTML must contain right: positioning for sliders");

        // Generate Arabic PDF
        byte[] pdfBytes = pdfService.generatePdfReport(reportContext, "ar");
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 50000, "Arabic PDF must be > 50KB");

        try (PDDocument doc = PDDocument.load(pdfBytes)) {
            assertEquals(14, doc.getNumberOfPages(), "Report must have 14 pages");
        }
        System.out.println("Arabic (RTL) PDF verified successfully! Size: " + pdfBytes.length + " bytes");
    }

    @Test
    @DisplayName("Test English (LTR) PDF Generation")
    public void testEnglishLtrPdfGeneration() throws Exception {
        LeadershipReportGeneratorService genService = new LeadershipReportGeneratorService(objectMapper);
        PdfGeneratorService pdfService = new PdfGeneratorService(templateEngine);

        ReportContextDto reportContext = genService.generateReport(createSampleScoreDto());
        assertNotNull(reportContext);

        // Generate English HTML & check dir / slider
        String html = pdfService.generateHtmlReport(reportContext, "en");
        assertTrue(html.contains("dir=\"ltr\""), "English HTML must have dir='ltr'");
        assertTrue(html.contains("left:"), "English HTML must contain left: positioning for sliders");
        assertTrue(html.contains("Results Summary"), "English HTML must contain English title from message bundle");
        assertTrue(html.contains("Behavioral Competencies"), "English HTML must contain English table header");

        // Generate English PDF
        byte[] pdfBytes = pdfService.generatePdfReport(reportContext, "en");
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 50000, "English PDF must be > 50KB");

        try (PDDocument doc = PDDocument.load(pdfBytes)) {
            assertEquals(14, doc.getNumberOfPages(), "Report must have 14 pages");
        }

        // Save PDF for inspection
        File outFile = new File("C:/Users/Logo/.gemini/antigravity/brain/117627c9-dbc7-4aff-b37d-4deddc3f231e/scratch/report_english_test.pdf");
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            fos.write(pdfBytes);
        }
        System.out.println("English (LTR) PDF verified successfully! Size: " + pdfBytes.length + " bytes. Saved to: " + outFile.getAbsolutePath());
    }
}
