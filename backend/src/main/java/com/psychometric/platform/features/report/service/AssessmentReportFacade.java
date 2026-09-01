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
     * @param forceRefresh if true, evicts caches and forces a fresh AI report generation
     * @return secure Cloudinary CDN download URL for the PDF report
     */
    @Transactional
    public String getOrGenerateReportPdfUrl(String attemptToken, boolean forceRefresh) {
        log.info("Processing assessment report request for attempt token: {} (forceRefresh={})", attemptToken, forceRefresh);

        AssessmentScore score = scoreRepository.findByAttemptAttemptToken(attemptToken)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment score not found for attempt token: " + attemptToken));

        // 1. If forceRefresh is requested, evict in-memory and section AI caches
        if (forceRefresh) {
            log.info("Force refresh requested: Clearing all caches for attempt {}", attemptToken);
            reportGeneratorService.clearCandidateCaches(attemptToken);
        } else {
            // 2. Cache Check: Return existing PDF URL if already generated and valid (.pdf extension)
            if (score.getReportPdfUrl() != null && !score.getReportPdfUrl().isBlank() && score.getReportPdfUrl().toLowerCase().endsWith(".pdf")) {
                log.info("Cache hit: Returning existing report PDF URL for attempt {}: {}", attemptToken, score.getReportPdfUrl());
                return score.getReportPdfUrl();
            }
        }

        log.info("Generating AI-driven leadership report PDF for attempt: {}", attemptToken);

        // 3. Fetch and Convert Raw Scoring Data
        AssessmentScoreResponseDto rawScoreDto = AssessmentScoreResponseDto.fromEntity(score);

        // 4. AI Normalization & Arabic Narrative Generation
        ReportContextDto reportContextDto = reportGeneratorService.generateReport(rawScoreDto);

        // 5. PDF Compilation using OpenHTMLtoPDF & Master Thymeleaf Template
        byte[] pdfBytes = pdfGeneratorService.generatePdfReport(reportContextDto);

        // 6. Upload to Cloudinary CDN
        String fileName = "leadership_report_" + attemptToken + ".pdf";
        String cloudinaryUrl = cloudinaryService.uploadPdf(pdfBytes, fileName, "psychometric/reports");

        // 7. Cache the generated URL in the database
        score.setReportPdfUrl(cloudinaryUrl);
        scoreRepository.save(score);

        log.info("Successfully completed end-to-end report generation pipeline for attempt {}. URL: {}", attemptToken, cloudinaryUrl);
        return cloudinaryUrl;
    }

    public String getOrGenerateReportPdfUrl(String attemptToken) {
        return getOrGenerateReportPdfUrl(attemptToken, false);
    }

    /**
     * Generates PDF bytes on-the-fly for direct streaming / local download without Cloudinary.
     */
    @Transactional(readOnly = true)
    public byte[] generateDirectPdfBytes(String attemptToken, boolean forceRefresh) {
        if (forceRefresh) {
            reportGeneratorService.clearCandidateCaches(attemptToken);
        }
        AssessmentScore score = scoreRepository.findByAttemptAttemptToken(attemptToken)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment score not found for attempt token: " + attemptToken));

        AssessmentScoreResponseDto rawScoreDto = AssessmentScoreResponseDto.fromEntity(score);
        ReportContextDto reportContextDto = reportGeneratorService.generateReport(rawScoreDto);
        return pdfGeneratorService.generatePdfReport(reportContextDto);
    }

    public byte[] generateDirectPdfBytes(String attemptToken) {
        return generateDirectPdfBytes(attemptToken, false);
    }
}
