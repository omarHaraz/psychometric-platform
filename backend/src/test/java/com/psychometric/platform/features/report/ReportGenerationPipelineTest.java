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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.spring6.SpringTemplateEngine;

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

        rawScore.getDerailerCategoryScores().addAll(List.of(
                new DerailerCategoryScoreDto(1L, "التحفظ", 1, 12.0, 60.0),
                new DerailerCategoryScoreDto(2L, "الانفعالية", 2, 10.0, 50.0),
                new DerailerCategoryScoreDto(3L, "العدائية", 3, 12.0, 60.0),
                new DerailerCategoryScoreDto(4L, "الاندفاعية", 4, 12.0, 60.0),
                new DerailerCategoryScoreDto(5L, "الصرامة", 5, 12.0, 60.0),
                new DerailerCategoryScoreDto(6L, "اللامألوفية", 6, 14.0, 70.0)
        ));

        rawScore.getGcatSubtestScores().addAll(List.of(
                new GcatSubtestScoreDto(GcatSubtestCode.ABSTRACT, 11, 14, 78.5),
                new GcatSubtestScoreDto(GcatSubtestCode.NUMERICAL, 12, 14, 85.7),
                new GcatSubtestScoreDto(GcatSubtestCode.VERBAL, 7, 14, 50.0)
        ));

        LeadershipReportGeneratorService generatorService = new LeadershipReportGeneratorService(objectMapper);
        
        ReportContextDto reportDto = generatorService.generateReport(rawScore);
        assertNotNull(reportDto);

        String socialInterpretation = reportDto.getSocialInterpretation();
        String impulsivityText = reportDto.getImpulsivityText();
        String page7Rec1 = reportDto.getCompetencyPage(7) != null ? reportDto.getCompetencyPage(7).getRec1() : null;

        assertNotNull(socialInterpretation, "socialInterpretation must not be null");
        assertNotNull(impulsivityText, "impulsivityText must not be null");
        assertNotNull(page7Rec1, "Page 7 rec1 must not be null");

        PdfGeneratorService pdfService = new PdfGeneratorService(templateEngine);
        String htmlContent = pdfService.generateHtmlReport(reportDto);
        assertNotNull(htmlContent, "HTML content must not be null");
        assertTrue(htmlContent.length() > 1000, "HTML must be a substantial document");

        byte[] pdfBytes = pdfService.generatePdfReport(reportDto);
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 50000, "PDF must be substantial (>50KB)");

        try (PDDocument doc = PDDocument.load(pdfBytes)) {
            int pageCount = doc.getNumberOfPages();
            assertEquals(14, pageCount, "PDF must have exactly 14 pages");

            // Render Page 5 (index 4) for visual inspection
            org.apache.pdfbox.rendering.PDFRenderer renderer = new org.apache.pdfbox.rendering.PDFRenderer(doc);
            java.awt.image.BufferedImage img = renderer.renderImageWithDPI(4, 150);
            java.io.File outImg = new java.io.File("C:/Users/Logo/.gemini/antigravity/brain/117627c9-dbc7-4aff-b37d-4deddc3f231e/page_5_verified.png");
            javax.imageio.ImageIO.write(img, "PNG", outImg);
            System.out.println("Saved Page 5 visual inspection artifact: " + outImg.getAbsolutePath());
        }
        
        System.out.println("Pipeline Test Completed Successfully");
    }
}
