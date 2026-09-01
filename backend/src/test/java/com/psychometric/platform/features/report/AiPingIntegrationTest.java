package com.psychometric.platform.features.report;

import com.psychometric.platform.features.report.service.GeminiAiReportClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
public class AiPingIntegrationTest {

    @Autowired
    private GeminiAiReportClient geminiClient;

    @Test
    void testDirectAiPing() {
        System.out.println("==================================================");
        System.out.println("TESTING ISOLATED GEMINI AI API PING");
        System.out.println("==================================================");

        String prompt = "اكتب توصية تطويرية من جملة واحدة لقائد يتمتع بمهارات تواصل عالية.";
        System.out.println("Prompt Sent: " + prompt);

        try {
            String result = geminiClient.callApi(prompt);
            System.out.println("--------------------------------------------------");
            System.out.println("AI RAW RESPONSE:");
            System.out.println(result);
            System.out.println("--------------------------------------------------");
            assertNotNull(result);
            assertFalse(result.isBlank());
        } catch (Exception e) {
            System.err.println("Gemini AI API Call Failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
