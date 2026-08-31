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
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent",
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
}
