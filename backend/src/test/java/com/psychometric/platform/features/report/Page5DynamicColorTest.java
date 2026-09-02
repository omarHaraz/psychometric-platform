package com.psychometric.platform.features.report;

import com.psychometric.platform.features.report.dto.ReportContextDto;
import com.psychometric.platform.features.report.service.LeadershipReportGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
public class Page5DynamicColorTest {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Test
    public void testGetTierColorLogic() {
        assertEquals("#388e3c", LeadershipReportGeneratorService.getTierColor(5.0));
        assertEquals("#388e3c", LeadershipReportGeneratorService.getTierColor(4.0));
        assertEquals("#388e3c", LeadershipReportGeneratorService.getTierColor(4.2));
        assertEquals("#d97736", LeadershipReportGeneratorService.getTierColor(3.99));
        assertEquals("#d97736", LeadershipReportGeneratorService.getTierColor(3.0));
        assertEquals("#d32f2f", LeadershipReportGeneratorService.getTierColor(2.99));
        assertEquals("#d32f2f", LeadershipReportGeneratorService.getTierColor(1.0));
        assertEquals("#1e3a4c", LeadershipReportGeneratorService.getTierColor(null));
    }

    @Test
    public void testPage5HtmlRendersDynamicColors() {
        ReportContextDto report = ReportContextDto.createDefaultReport("TEST_CANDIDATE");
        report.setCommScore(4.5);
        report.setCommColor(LeadershipReportGeneratorService.getTierColor(4.5)); // #388e3c

        report.setInitiativeScore(3.2);
        report.setInitiativeColor(LeadershipReportGeneratorService.getTierColor(3.2)); // #d97736

        report.setDecisionScore(2.1);
        report.setDecisionColor(LeadershipReportGeneratorService.getTierColor(2.1)); // #d32f2f

        Context context = new Context(new Locale("ar"));
        context.setVariables(report.toFlatMap());

        String html = templateEngine.process("report/page5", context);

        // Verify that Green #388e3c, Orange #d97736, and Red #d32f2f appear in the generated HTML
        assertTrue(html.contains("#388e3c"), "Page 5 should contain Green color for high scores");
        assertTrue(html.contains("#d97736"), "Page 5 should contain Orange color for moderate scores");
        assertTrue(html.contains("#d32f2f"), "Page 5 should contain Red color for low scores");
    }
}
