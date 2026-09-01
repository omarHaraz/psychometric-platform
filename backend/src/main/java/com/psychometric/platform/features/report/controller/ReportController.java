package com.psychometric.platform.features.report.controller;

import com.psychometric.platform.features.report.service.AssessmentReportFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private final AssessmentReportFacade reportFacade;

    public ReportController(AssessmentReportFacade reportFacade) {
        this.reportFacade = reportFacade;
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
     */
    @GetMapping({
            "/assessments/{token}/report/pdf",
            "/reports/{token}/pdf"
    })
    @Operation(summary = "Direct PDF Stream", description = "Streams the raw PDF bytes directly to the browser for instant download")
    public ResponseEntity<byte[]> streamReportPdf(
            @PathVariable("token") String token,
            @RequestParam(name = "forceRefresh", defaultValue = "false") boolean forceRefresh
    ) {
        byte[] pdfBytes = reportFacade.generateDirectPdfBytes(token, forceRefresh);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"leadership_report_" + token + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }
}
