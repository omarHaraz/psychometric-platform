package com.psychometric.platform.features.report;

import com.openhtmltopdf.bidi.support.ICUBidiReorderer;
import com.openhtmltopdf.bidi.support.ICUBidiSplitter;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ManualPdfCompilationTest {

    @Test
    public void compileManualToPdf() throws Exception {
        String htmlPath = "C:/Users/Logo/.gemini/antigravity/brain/117627c9-dbc7-4aff-b37d-4deddc3f231e/manual.html";
        String pdfPath = "C:/Users/Logo/.gemini/antigravity/brain/117627c9-dbc7-4aff-b37d-4deddc3f231e/PSYCHOMETRIC_TECHNICAL_REFERENCE_MANUAL_AR.pdf";

        String htmlContent = Files.readString(Paths.get(htmlPath));

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.useFont(() -> getClass().getResourceAsStream("/fonts/Cairo-Regular.ttf"), "Cairo");
        builder.useFont(() -> getClass().getResourceAsStream("/fonts/Amiri-Regular.ttf"), "Amiri");
        builder.useFont(() -> getClass().getResourceAsStream("/fonts/Amiri-Bold.ttf"), "Amiri", 700, PdfRendererBuilder.FontStyle.NORMAL, true);
        builder.useUnicodeBidiSplitter(new ICUBidiSplitter.ICUBidiSplitterFactory());
        builder.useUnicodeBidiReorderer(new ICUBidiReorderer());
        builder.defaultTextDirection(PdfRendererBuilder.TextDirection.RTL);
        builder.withHtmlContent(htmlContent, new File(htmlPath).getParentFile().toURI().toString());

        File outputFile = new File(pdfPath);
        try (FileOutputStream os = new FileOutputStream(outputFile)) {
            builder.toStream(os);
            builder.run();
        }

        System.out.println("================================================================================");
        System.out.println("Successfully compiled complete Arabic Psychometric Reference Manual to PDF:");
        System.out.println("  PDF Location: " + outputFile.getAbsolutePath());
        System.out.println("  File Size: " + outputFile.length() + " bytes");
        System.out.println("================================================================================");
    }
}
