package com.psychometric.platform.features.report.controller;

import com.psychometric.platform.features.report.service.AssessmentReportFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

/**
 * ReportController
 *
 * Exposes REST endpoints to generate, retrieve, and download comprehensive
 * AI-driven PDF leadership assessment reports.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.OPTIONS})
@Tag(name = "Assessment Reports", description = "Endpoints for generating and downloading leadership assessment PDF reports")
public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    private final AssessmentReportFacade reportFacade;
    private final SpringTemplateEngine templateEngine;

    public ReportController(AssessmentReportFacade reportFacade, SpringTemplateEngine templateEngine) {
        this.reportFacade = reportFacade;
        this.templateEngine = templateEngine;
    }

    /**
     * Triggers report generation pipeline or returns cached Cloudinary CDN URL.
     * Use forceRefresh=true to bypass database cache and evict in-memory AI caches.
     */
    @GetMapping({
            "/assessments/{token}/report/download",
            "/reports/{token}/download"
    })
    @Operation(summary = "Get or generate report download URL", description = "Returns the secure Cloudinary CDN URL for the leadership assessment PDF report")
    public ResponseEntity<Map<String, Object>> getReportDownloadUrl(
            @PathVariable("token") String token,
            @RequestParam(name = "forceRefresh", defaultValue = "false") boolean forceRefresh
    ) {
        String reportUrl = reportFacade.getOrGenerateReportPdfUrl(token, forceRefresh);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "attemptToken", token,
                "reportUrl", reportUrl,
                "forceRefreshed", forceRefresh
        ));
    }

    /**
     * Direct binary streaming download endpoint for environments without Cloudinary.
     * Use forceRefresh=true to bypass database cache and evict in-memory AI caches.
     * If the assessment is incomplete, serves a modern Arabic informative notification page.
     */
    @GetMapping({
            "/assessments/{token}/report/pdf",
            "/reports/{token}/pdf"
    })
    @Operation(summary = "Direct PDF Stream", description = "Streams the raw PDF bytes directly to the browser for instant download")
    public ResponseEntity<?> streamReportPdf(
            @PathVariable("token") String token,
            @RequestParam(name = "forceRefresh", defaultValue = "false") boolean forceRefresh
    ) {
        try {
            byte[] pdfBytes = reportFacade.generateDirectPdfBytes(token, forceRefresh);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"leadership_report_" + token + ".pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);
        } catch (Exception ex) {
            log.warn("Report generation blocked for attempt token {}: {}", token, ex.getMessage());
            Context context = new Context(new Locale("ar"));
            context.setVariable("attemptToken", token);
            context.setVariable("errorMessage", ex.getMessage());
            String html = templateEngine.process("error/incomplete-assessment", context);

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("text/html;charset=UTF-8"))
                    .body(html);
        }
    }
}
