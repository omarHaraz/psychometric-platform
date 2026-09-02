package com.psychometric.platform.features.report;

import com.psychometric.platform.features.report.dto.ReportContextDto.CompetencyDetailDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
public class ConditionalRowRenderingTest {

    @Autowired
    private SpringTemplateEngine templateEngine;

    private static final List<String> TEMPLATES = List.of(
            "report/page6",
            "report/page7",
            "report/page8",
            "report/page9",
            "report/page10",
            "report/page11",
            "report/page12",
            "report/page13",
            "report/competency-detail"
    );

    private int countTbodyRows(String html) {
        // Extract <tbody> ... </tbody>
        Pattern tbodyPattern = Pattern.compile("(?s)<tbody>(.*?)</tbody>");
        Matcher tbodyMatcher = tbodyPattern.matcher(html);
        if (!tbodyMatcher.find()) {
            return 0;
        }
        String tbodyContent = tbodyMatcher.group(1);
        // Count <tr occurrences inside <tbody>
        Pattern trPattern = Pattern.compile("<tr[\\s>]");
        Matcher trMatcher = trPattern.matcher(tbodyContent);
        int count = 0;
        while (trMatcher.find()) {
            count++;
        }
        return count;
    }

    @Test
    public void testCleanOrNullJavaSanitization() {
        assertNull(CompetencyDetailDto.cleanOrNull(null));
        assertNull(CompetencyDetailDto.cleanOrNull(""));
        assertNull(CompetencyDetailDto.cleanOrNull("   "));
        assertNull(CompetencyDetailDto.cleanOrNull("\u00A0\u200B\u3000"));
        assertNull(CompetencyDetailDto.cleanOrNull("null"));
        assertNull(CompetencyDetailDto.cleanOrNull("NULL"));
        assertNull(CompetencyDetailDto.cleanOrNull("-"));
        assertNull(CompetencyDetailDto.cleanOrNull("—"));
        assertNull(CompetencyDetailDto.cleanOrNull("abc"));
        assertNull(CompetencyDetailDto.cleanOrNull("12345"));
        assertEquals("ينصت باهتمام ويتفاعل بإيجابية", CompetencyDetailDto.cleanOrNull("  \u00A0ينصت باهتمام ويتفاعل بإيجابية\u3000  "));
    }

    @Test
    public void testEmptyAiDataRendersZeroTableRows() {
        for (String templateName : TEMPLATES) {
            Context context = new Context(new Locale("ar"));
            context.setVariable("candidateId", "PCIV126371");
            context.setVariable("competencyTitle", "اختبار الكفاءة");
            // req1, req2, req3 are null

            String html = templateEngine.process(templateName, context);
            int rowCount = countTbodyRows(html);

            assertEquals(0, rowCount, "Template " + templateName + " should render 0 tbody rows when req1, req2, req3 are null");
        }
    }

    @Test
    public void testEmptyStringAiDataRendersZeroTableRows() {
        for (String templateName : TEMPLATES) {
            Context context = new Context(new Locale("ar"));
            context.setVariable("candidateId", "PCIV126371");
            context.setVariable("competencyTitle", "اختبار الكفاءة");
            context.setVariable("req1", "");
            context.setVariable("req2", "");
            context.setVariable("req3", "");

            String html = templateEngine.process(templateName, context);
            int rowCount = countTbodyRows(html);

            assertEquals(0, rowCount, "Template " + templateName + " should render 0 tbody rows when req1, req2, req3 are empty strings");
        }
    }

    @Test
    @org.junit.jupiter.api.Disabled("User requested simplified #strings.trim() logic which doesn't strip unicode")
    public void testUnicodeWhitespaceOnlyAiDataRendersZeroTableRows() {
        for (String templateName : TEMPLATES) {
            Context context = new Context(new Locale("ar"));
            context.setVariable("candidateId", "PCIV126371");
            context.setVariable("competencyTitle", "اختبار الكفاءة");
            context.setVariable("req1", "\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0"); // > 5 non-breaking spaces
            context.setVariable("req2", "\u200B\u200B\u200B\u200B\u200B\u200B"); // > 5 zero-width spaces
            context.setVariable("req3", "\u3000\u3000\u3000\u3000\u3000\u3000"); // > 5 ideographic spaces

            String html = templateEngine.process(templateName, context);
            int rowCount = countTbodyRows(html);

            assertEquals(0, rowCount, "Template " + templateName + " should render 0 tbody rows when req contains only Unicode whitespace");
        }
    }

    @Test
    @org.junit.jupiter.api.Disabled("User requested simplified HTML logic that only checks req length")
    public void testMissingRecOrResultRendersZeroRows() {
        for (String templateName : TEMPLATES) {
            Context context = new Context(new Locale("ar"));
            context.setVariable("candidateId", "PCIV126371");
            context.setVariable("competencyTitle", "اختبار الكفاءة");
            // Valid req, but null or empty rec & result
            context.setVariable("req1", "ينصت باهتمام ويتفاعل بإيجابية مع الآخرين لبناء علاقات مهنية قوية.");
            context.setVariable("rec1", null);
            context.setVariable("result1", "");
            context.setVariable("indicator1Color", "#558b6e");

            context.setVariable("req2", "يعبّر عن الأفكار والمشاعر بوضوح وثقة للتأثير في الآخرين.");
            context.setVariable("rec2", "   ");
            context.setVariable("result2", null);
            context.setVariable("indicator2Color", "#d98a44");

            context.setVariable("req3", "يتحاور بمرونة وتفهم، مقدراً وجهات النظر المختلفة للوصول إلى حلول توافقية.");
            context.setVariable("rec3", "\u00A0\u00A0\u00A0");
            context.setVariable("result3", "\u3000\u200B");
            context.setVariable("indicator3Color", "#d9776c");

            String html = templateEngine.process(templateName, context);
            int rowCount = countTbodyRows(html);

            assertEquals(0, rowCount, "Template " + templateName + " should render 0 tbody rows when rec or result are missing/empty");
        }
    }

    @Test
    public void testTwoRowsRenderedWhenTwoReqPresentWithRecAndResult() {
        for (String templateName : TEMPLATES) {
            Context context = new Context(new Locale("ar"));
            context.setVariable("candidateId", "PCIV126371");
            context.setVariable("competencyTitle", "اختبار الكفاءة");
            context.setVariable("req1", "ينصت باهتمام ويتفاعل بإيجابية مع الآخرين لبناء علاقات مهنية قوية.");
            context.setVariable("rec1", "توصية تفصيلية");
            context.setVariable("result1", "نتيجة تفصيلية");
            context.setVariable("indicator1Color", "#558b6e");

            context.setVariable("req2", "يعبّر عن الأفكار والمشاعر بوضوح وثقة للتأثير في الآخرين.");
            context.setVariable("rec2", "توصية تفصيلية");
            context.setVariable("result2", "نتيجة تفصيلية");
            context.setVariable("indicator2Color", "#d98a44");

            // Row 3 is empty
            context.setVariable("req3", null);

            String html = templateEngine.process(templateName, context);
            int rowCount = countTbodyRows(html);

            assertEquals(2, rowCount, "Template " + templateName + " should render exactly 2 tbody rows when 2 complete rows are present");
        }
    }

    @Test
    public void testThreeRowsRenderedWhenAllThreeReqPresentWithRecAndResult() {
        for (String templateName : TEMPLATES) {
            Context context = new Context(new Locale("ar"));
            context.setVariable("candidateId", "PCIV126371");
            context.setVariable("competencyTitle", "اختبار الكفاءة");
            context.setVariable("req1", "ينصت باهتمام ويتفاعل بإيجابية مع الآخرين لبناء علاقات مهنية قوية.");
            context.setVariable("rec1", "توصية تفصيلية");
            context.setVariable("result1", "نتيجة تفصيلية");
            context.setVariable("indicator1Color", "#558b6e");

            context.setVariable("req2", "يعبّر عن الأفكار والمشاعر بوضوح وثقة للتأثير في الآخرين.");
            context.setVariable("rec2", "توصية تفصيلية");
            context.setVariable("result2", "نتيجة تفصيلية");
            context.setVariable("indicator2Color", "#d98a44");

            context.setVariable("req3", "يتحاور بمرونة وتفهم، مقدراً وجهات النظر المختلفة للوصول إلى حلول توافقية.");
            context.setVariable("rec3", "توصية تفصيلية");
            context.setVariable("result3", "نتيجة تفصيلية");
            context.setVariable("indicator3Color", "#d9776c");

            String html = templateEngine.process(templateName, context);
            int rowCount = countTbodyRows(html);

            assertEquals(3, rowCount, "Template " + templateName + " should render exactly 3 tbody rows when all 3 complete rows are present");
        }
    }
}
