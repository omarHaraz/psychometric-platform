package com.psychometric.platform.features.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.features.report.dto.ReportContextDto;
import com.psychometric.platform.features.report.dto.ReportContextDto.CompetencyDetailDto;
import com.psychometric.platform.features.report.service.PdfGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

@SpringBootTest
@ActiveProfiles("local")
public class CompetencyScaleAlignmentTest {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Test
    public void testRenderAllScores1Through5() throws Exception {
        File outDir = new File("C:/Users/Logo/.gemini/antigravity/brain/117627c9-dbc7-4aff-b37d-4deddc3f231e/scratch/scores_test");
        outDir.mkdirs();

        for (int score = 1; score <= 5; score++) {
            CompetencyDetailDto comp = ReportContextDto.getDefaultCompetencyPage(7, "PCIV126371");
            comp.setCompetencyScore(score);
            comp.setCompetencyTitle("التواصل والتأثير الفعال");
            comp.setCompetencyColor("#1e3a4c");

            // Process competency-detail template
            Context context = new Context(new Locale("ar"));
            context.setVariables(comp.toMap());

            String html = templateEngine.process("report/competency-detail", context);

            // Wrap in minimal master HTML with styles if needed or render via OpenHTMLtoPDF
            com.openhtmltopdf.pdfboxout.PdfRendererBuilder builder = new com.openhtmltopdf.pdfboxout.PdfRendererBuilder();
            builder.useFastMode();
            builder.useFont(() -> getClass().getResourceAsStream("/fonts/Cairo-Regular.ttf"), "Cairo");
            builder.useFont(() -> getClass().getResourceAsStream("/fonts/Amiri-Regular.ttf"), "Amiri");
            builder.useFont(() -> getClass().getResourceAsStream("/fonts/Amiri-Bold.ttf"), "Amiri", 700, com.openhtmltopdf.pdfboxout.PdfRendererBuilder.FontStyle.NORMAL, true);
            builder.useUnicodeBidiSplitter(new com.openhtmltopdf.bidi.support.ICUBidiSplitter.ICUBidiSplitterFactory());
            builder.useUnicodeBidiReorderer(new com.openhtmltopdf.bidi.support.ICUBidiReorderer());
            builder.defaultTextDirection(com.openhtmltopdf.pdfboxout.PdfRendererBuilder.TextDirection.RTL);
            builder.withHtmlContent(html, "");

            File pdfFile = new File(outDir, "competency_score_" + score + ".pdf");
            try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
                builder.toStream(fos);
                builder.run();
            }

            System.out.println("Generated test PDF for score " + score + ": " + pdfFile.getAbsolutePath());
        }
    }
}
