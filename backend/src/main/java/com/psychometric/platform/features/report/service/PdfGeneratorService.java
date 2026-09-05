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
     * Renders the master Thymeleaf report template into an HTML string with default Arabic locale.
     */
    public String generateHtmlReport(ReportContextDto reportDto) {
        return generateHtmlReport(reportDto, "ar");
    }

    /**
     * Renders the master Thymeleaf report template into an HTML string with specific language (ar / en).
     */
    public String generateHtmlReport(ReportContextDto reportDto, String lang) {
        if (reportDto == null) {
            throw new IllegalArgumentException("ReportContextDto cannot be null");
        }
        String normalizedLang = (lang != null && lang.trim().equalsIgnoreCase("en")) ? "en" : "ar";
        String dir = normalizedLang.equals("ar") ? "rtl" : "ltr";

        Context context = reportDto.toThymeleafContext();
        context.setLocale(new Locale(normalizedLang));
        context.setVariable("lang", normalizedLang);
        context.setVariable("dir", dir);
        context.setVariable("currentDate", reportDto.getReportDate() != null ? reportDto.getReportDate() : java.time.LocalDate.now().toString());
        String logoB64 = getLogoBase64();
        if (logoB64 != null) {
            context.setVariable("companyLogoBase64", logoB64);
        }
        return templateEngine.process("report/master-report", context);
    }

    private String getLogoBase64() {
        try (var is = getClass().getResourceAsStream("/static/assets/images/logo.png")) {
            if (is != null) {
                byte[] bytes = is.readAllBytes();
                return "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(bytes);
            }
        } catch (Exception e) {
            log.warn("Could not load logo for PDF header/footer: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Generates a complete PDF document byte array from a populated {@link ReportContextDto}.
     */
    public byte[] generatePdfReport(ReportContextDto reportDto) {
        return generatePdfReport(reportDto, "ar");
    }

    /**
     * Generates a complete PDF document byte array with specific language (ar / en).
     */
    public byte[] generatePdfReport(ReportContextDto reportDto, String lang) {
        if (reportDto == null) {
            throw new IllegalArgumentException("ReportContextDto cannot be null");
        }

        String normalizedLang = (lang != null && lang.trim().equalsIgnoreCase("en")) ? "en" : "ar";
        String dir = normalizedLang.equals("ar") ? "rtl" : "ltr";
        boolean isRtl = normalizedLang.equals("ar");

        log.info("================================================================================");
        log.info("[PDF PIPELINE AUDIT] Generating PDF report for candidate: {} ({}) | Lang: {} | Dir: {}",
                reportDto.getCandidateId(), reportDto.getCandidateName(), normalizedLang, dir);
        log.info("================================================================================");

        try {
            // 1. Process Thymeleaf Master Report Template into HTML String
            String htmlContent = generateHtmlReport(reportDto, normalizedLang);

            // 2. Sanitize HTML -> XHTML: OpenHTMLtoPDF parses as strict XML.
            htmlContent = htmlContent.replaceAll("&(?!(amp|lt|gt|quot|apos|#\\d+|#x[0-9a-fA-F]+);)", "&amp;");

            // 3. Build PDF using OpenHTMLtoPDF with language-aware text direction
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                
                // Explicitly register local TrueType fonts from classpath
                builder.useFont(() -> getClass().getResourceAsStream("/fonts/Cairo-Regular.ttf"), "Cairo");
                builder.useFont(() -> getClass().getResourceAsStream("/fonts/Amiri-Regular.ttf"), "Amiri");
                builder.useFont(() -> getClass().getResourceAsStream("/fonts/Amiri-Bold.ttf"), "Amiri", 700, PdfRendererBuilder.FontStyle.NORMAL, true);

                // Enable bidirectional text splitting, reordering and text direction
                builder.useUnicodeBidiSplitter(new ICUBidiSplitter.ICUBidiSplitterFactory());
                builder.useUnicodeBidiReorderer(new ICUBidiReorderer());
                builder.defaultTextDirection(isRtl ? PdfRendererBuilder.TextDirection.RTL : PdfRendererBuilder.TextDirection.LTR);
                
                String baseUri = "";
                try {
                    var staticUrl = getClass().getResource("/static/");
                    if (staticUrl != null) {
                        baseUri = staticUrl.toExternalForm();
                    }
                } catch (Exception ignored) {}

                builder.withHtmlContent(htmlContent, baseUri);
                builder.toStream(outputStream);
                builder.run();

                byte[] pdfBytes = outputStream.toByteArray();
                log.info("Successfully generated PDF report ({} bytes) in {} (dir={}) for candidate: {}",
                        pdfBytes.length, normalizedLang, dir, reportDto.getCandidateId());
                return pdfBytes;
            }

        } catch (Exception e) {
            log.error("Failed to generate PDF report: {}", e.getMessage(), e);
            throw new RuntimeException("PDF Generation failed: " + e.getMessage(), e);
        }
    }
}
