package com.psychometric.platform.features.report.controller;

import com.psychometric.platform.features.report.service.GeminiAiReportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Isolated test controller to verify the external AI API integration
 * without executing PDF or report generation logic.
 */
@RestController
@RequestMapping("/api/test")
public class AiTestController {

    private static final Logger log = LoggerFactory.getLogger(AiTestController.class);

    private final GeminiAiReportClient aiService;

    public AiTestController(GeminiAiReportClient aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/ai-ping")
    public ResponseEntity<?> testAi(@RequestParam(required = false) String customPrompt) {
        log.info("Starting isolated AI API test...");
        String prompt = (customPrompt != null && !customPrompt.isBlank())
                ? customPrompt
                : "اكتب توصية تطويرية من جملة واحدة لقائد يتمتع بمهارات تواصل عالية.";

        try {
            log.info("Calling Gemini AI with prompt: {}", prompt);
            String response = aiService.callApi(prompt);
            log.info("AI API Response: {}", response);
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "prompt", prompt,
                    "response", response
            ));
        } catch (Exception e) {
            log.error("AI API Test Failed", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "ERROR",
                    "error", e.getMessage() != null ? e.getMessage() : e.toString()
            ));
        }
    }
}
