package com.psychometric.platform.features.report.service;

import com.openhtmltopdf.bidi.support.ICUBidiReorderer;
import com.openhtmltopdf.bidi.support.ICUBidiSplitter;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.psychometric.platform.features.report.dto.ReportContextDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

/**
 * PdfGeneratorService
 *
 * Renders the master Thymeleaf report template into an HTML string and converts
 * it into a high-fidelity PDF byte array with full Arabic text shaping, RTL support,
 * and precise A4 pagination using OpenHTMLtoPDF.
 */
@Service
public class PdfGeneratorService {

    private static final Logger log = LoggerFactory.getLogger(PdfGeneratorService.class);

    private final SpringTemplateEngine templateEngine;

    public PdfGeneratorService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Generates a complete 15-page PDF document byte array from a populated {@link ReportContextDto}.
     *
     * @param reportDto the fully populated report context data model
     * @return PDF content as byte array
     */
    public byte[] generatePdfReport(ReportContextDto reportDto) {
        if (reportDto == null) {
            throw new IllegalArgumentException("ReportContextDto cannot be null");
        }

        log.info("Generating PDF report for candidate: {}", reportDto.getCandidateId());

        try {
            // 1. Process Thymeleaf Master Report Template into HTML String
            Context context = reportDto.toThymeleafContext();
            context.setLocale(new Locale("ar"));
            String htmlContent = templateEngine.process("report/master-report", context);

            // 2. Build PDF using OpenHTMLtoPDF with RTL text support & Unicode Bidirectional shaping
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                
                // Enable bidirectional text splitting, reordering and RTL shaping for Arabic
                builder.useUnicodeBidiSplitter(new ICUBidiSplitter.ICUBidiSplitterFactory());
                builder.useUnicodeBidiReorderer(new ICUBidiReorderer());
                builder.defaultTextDirection(PdfRendererBuilder.TextDirection.RTL);
                
                builder.withHtmlContent(htmlContent, "");
                builder.toStream(outputStream);
                builder.run();

                byte[] pdfBytes = outputStream.toByteArray();
                log.info("Successfully generated PDF report ({} bytes) for candidate: {}", pdfBytes.length, reportDto.getCandidateId());
                return pdfBytes;
            }

        } catch (Exception e) {
            log.error("Failed to generate PDF report: {}", e.getMessage(), e);
            throw new RuntimeException("PDF Generation failed: " + e.getMessage(), e);
        }
    }
}
