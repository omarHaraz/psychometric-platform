package com.psychometric.platform.features.report.service;

import com.psychometric.platform.common.exception.ResourceNotFoundException;
import com.psychometric.platform.features.assessment.domain.model.AssessmentScore;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto;
import com.psychometric.platform.features.assessment.repository.AssessmentScoreRepository;
import com.psychometric.platform.features.itembank.common.service.CloudinaryService;
import com.psychometric.platform.features.report.dto.ReportContextDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AssessmentReportFacade
 *
 * Coordinates the full end-to-end report generation lifecycle:
 * 1. Cache Check: returns existing Cloudinary URL if report was already generated.
 * 2. Raw Data Fetch: retrieves AssessmentScore from the database.
 * 3. AI Narrative Generation: calls LeadershipReportGeneratorService.
 * 4. PDF Compilation: compiles master-report.html into PDF bytes using PdfGeneratorService.
 * 5. Cloud CDN Upload: uploads the PDF to Cloudinary and obtains secure download URL.
 * 6. Persistence: caches the URL in the database for instant subsequent retrievals.
 */
@Service
public class AssessmentReportFacade {

    private static final Logger log = LoggerFactory.getLogger(AssessmentReportFacade.class);

    private final AssessmentScoreRepository scoreRepository;
    private final LeadershipReportGeneratorService reportGeneratorService;
    private final PdfGeneratorService pdfGeneratorService;
    private final CloudinaryService cloudinaryService;

    public AssessmentReportFacade(
            AssessmentScoreRepository scoreRepository,
            LeadershipReportGeneratorService reportGeneratorService,
            PdfGeneratorService pdfGeneratorService,
            CloudinaryService cloudinaryService
    ) {
        this.scoreRepository = scoreRepository;
        this.reportGeneratorService = reportGeneratorService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.cloudinaryService = cloudinaryService;
    }

    /**
     * Generates or retrieves the cached PDF report download URL for the given assessment attempt token.
     *
     * @param attemptToken the unique assessment attempt token
     * @return secure Cloudinary CDN download URL for the PDF report
     */
    @Transactional
    public String getOrGenerateReportPdfUrl(String attemptToken) {
        log.info("Processing assessment report request for attempt token: {}", attemptToken);

        AssessmentScore score = scoreRepository.findByAttemptAttemptToken(attemptToken)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment score not found for attempt token: " + attemptToken));

        // 1. Cache Check: Return existing PDF URL if already generated and valid (.pdf extension)
        if (score.getReportPdfUrl() != null && !score.getReportPdfUrl().isBlank() && score.getReportPdfUrl().toLowerCase().endsWith(".pdf")) {
            log.info("Cache hit: Returning existing report PDF URL for attempt {}: {}", attemptToken, score.getReportPdfUrl());
            return score.getReportPdfUrl();
        }

        log.info("Cache miss or regenerating valid .pdf URL: Generating AI-driven leadership report PDF for attempt: {}", attemptToken);

        // 2. Fetch and Convert Raw Scoring Data
        AssessmentScoreResponseDto rawScoreDto = AssessmentScoreResponseDto.fromEntity(score);

        // 3. AI Normalization & Arabic Narrative Generation
        ReportContextDto reportContextDto = reportGeneratorService.generateReport(rawScoreDto);

        // 4. PDF Compilation using OpenHTMLtoPDF & Master Thymeleaf Template
        byte[] pdfBytes = pdfGeneratorService.generatePdfReport(reportContextDto);

        // 5. Upload to Cloudinary CDN
        String fileName = "leadership_report_" + attemptToken + ".pdf";
        String cloudinaryUrl = cloudinaryService.uploadPdf(pdfBytes, fileName, "psychometric/reports");

        // 6. Cache the generated URL in the database
        score.setReportPdfUrl(cloudinaryUrl);
        scoreRepository.save(score);

        log.info("Successfully completed end-to-end report generation pipeline for attempt {}. URL: {}", attemptToken, cloudinaryUrl);
        return cloudinaryUrl;
    }

    /**
     * Generates PDF bytes on-the-fly for direct streaming / local download without Cloudinary.
     */
    @Transactional(readOnly = true)
    public byte[] generateDirectPdfBytes(String attemptToken) {
        AssessmentScore score = scoreRepository.findByAttemptAttemptToken(attemptToken)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment score not found for attempt token: " + attemptToken));

        AssessmentScoreResponseDto rawScoreDto = AssessmentScoreResponseDto.fromEntity(score);
        ReportContextDto reportContextDto = reportGeneratorService.generateReport(rawScoreDto);
        return pdfGeneratorService.generatePdfReport(reportContextDto);
    }
}
