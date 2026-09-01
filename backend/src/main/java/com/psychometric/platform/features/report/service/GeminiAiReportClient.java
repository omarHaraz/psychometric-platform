package com.psychometric.platform.features.report.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.features.report.service.LeadershipReportGeneratorService.AiPromptPayload;
import com.psychometric.platform.features.report.service.LeadershipReportGeneratorService.AiReportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * GeminiAiReportClient
 *
 * Implements the {@link AiReportClient} interface to call Google Gemini API using Spring RestClient.
 * Passes the normalized psychometric scores and instructions, requesting strict minified JSON output.
 */
@Component
@Primary
public class GeminiAiReportClient implements AiReportClient {

    private static final Logger log = LoggerFactory.getLogger(GeminiAiReportClient.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String geminiApiUrl;
    private final String geminiApiKey;

    @org.springframework.beans.factory.annotation.Autowired
    public GeminiAiReportClient(
            ObjectMapper objectMapper,
            @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent}") String geminiApiUrl,
            @Value("${gemini.api.key:}") String geminiApiKey
    ) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(15));
        requestFactory.setReadTimeout(Duration.ofSeconds(45));

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.objectMapper = objectMapper;
        this.geminiApiUrl = geminiApiUrl;
        this.geminiApiKey = geminiApiKey != null ? geminiApiKey.trim() : "";
    }

    public GeminiAiReportClient(
            Object webClientBuilderIgnored,
            ObjectMapper objectMapper,
            String geminiApiUrl,
            String geminiApiKey
    ) {
        this(objectMapper, geminiApiUrl, geminiApiKey);
    }

    @Override
    public String generateNarrativesJson(AiPromptPayload payload) {
        if (geminiApiKey.isBlank()) {
            log.warn("Gemini API key is not configured. Using high-quality default psychometric narratives fallback.");
            return new LeadershipReportGeneratorService.DefaultMockAiClient().generateNarrativesJson(payload);
        }

        try {
            String promptText = buildPromptText(payload);

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", promptText)))
                    ),
                    "generationConfig", Map.of(
                            "responseMimeType", "application/json",
                            "temperature", 0.2
                    )
            );

            String url = geminiApiUrl.contains("key=")
                    ? geminiApiUrl
                    : (geminiApiUrl + (geminiApiUrl.contains("?") ? "&key=" : "?key=") + geminiApiKey);

            log.info("Sending request to Gemini AI for candidate narrative generation...");

            String responseBody = restClient.post()
                    .uri(url)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            if (responseBody == null || responseBody.isBlank()) {
                throw new IllegalStateException("Empty response received from Gemini API");
            }

            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode parts = candidates.get(0).path("content").path("parts");
                if (parts.isArray() && !parts.isEmpty()) {
                    String extractedText = parts.get(0).path("text").asText();
                    log.info("Successfully received AI-generated narratives from Gemini API.");
                    return extractedText;
                }
            }

            throw new IllegalStateException("Unexpected Gemini API response structure: " + responseBody);

        } catch (Exception e) {
            log.error("Gemini AI generation failed ({}). Falling back to standard default psychometric narratives.", e.getMessage());
            return new LeadershipReportGeneratorService.DefaultMockAiClient().generateNarrativesJson(payload);
        }
    }

    /**
     * Direct API call for testing and generic prompt generation.
     */
    public String callApi(String prompt) {
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            throw new IllegalStateException("Gemini API key is not configured (gemini.api.key is empty).");
        }

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                ),
                "generationConfig", Map.of(
                        "temperature", 0.7
                )
        );

        String url = geminiApiUrl.contains("key=")
                ? geminiApiUrl
                : (geminiApiUrl + (geminiApiUrl.contains("?") ? "&key=" : "?key=") + geminiApiKey);

        log.info("Sending request to Gemini AI API: URL={}", geminiApiUrl);

        String responseBody = restClient.post()
                .uri(url)
                .body(requestBody)
                .retrieve()
                .body(String.class);

        if (responseBody == null || responseBody.isBlank()) {
            throw new IllegalStateException("Empty response received from Gemini API");
        }

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode parts = candidates.get(0).path("content").path("parts");
                if (parts.isArray() && !parts.isEmpty()) {
                    return parts.get(0).path("text").asText();
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse Gemini response JSON as standard format: {}", e.getMessage());
        }

        return responseBody;
    }

    private String buildPromptText(AiPromptPayload payload) {
        try {
            String scoresJson = objectMapper.writeValueAsString(payload.normalizedScores());
            String benchmarksJson = objectMapper.writeValueAsString(payload.roleBenchmarks());

            return """
                    %s
                    
                    Candidate Identifier: %s
                    Candidate Name: %s
                    
                    Normalized Scores:
                    %s
                    
                    Role Benchmarks:
                    %s
                    
                    Competencies:
                    %s
                    
                    Return ONLY a minified JSON object with the following schema:
                    {
                      "socialInterpretation": "string",
                      "centralInterpretation": "string",
                      "reservedText": "string",
                      "emotionalityText": "string",
                      "hostilityText": "string",
                      "impulsivityText": "string",
                      "rigidityText": "string",
                      "unconventionalityText": "string",
                      "competencyNarratives": {
                        "7": {"result1": "...", "rec1": "...", "result2": "...", "rec2": "...", "result3": "...", "rec3": "..."},
                        "8": {"result1": "...", "rec1": "...", "result2": "...", "rec2": "...", "result3": "...", "rec3": "..."},
                        "9": {"result1": "...", "rec1": "...", "result2": "...", "rec2": "...", "result3": "...", "rec3": "..."},
                        "10": {"result1": "...", "rec1": "...", "result2": "...", "rec2": "...", "result3": "...", "rec3": "..."},
                        "11": {"result1": "...", "rec1": "...", "result2": "...", "rec2": "..."},
                        "12": {"result1": "...", "rec1": "...", "result2": "...", "rec2": "...", "result3": "...", "rec3": "..."},
                        "13": {"result1": "...", "rec1": "...", "result2": "...", "rec2": "...", "result3": "...", "rec3": "..."},
                        "14": {"result1": "...", "rec1": "...", "result2": "...", "rec2": "...", "result3": "...", "rec3": "..."}
                      },
                      "growGoalText": "string",
                      "growRealityText": "string",
                      "growOptionsText": "string",
                      "growWillText": "string"
                    }
                    """.formatted(
                    payload.instructions(),
                    payload.candidateId(),
                    payload.candidateName() != null ? payload.candidateName() : "المرشح",
                    scoresJson,
                    benchmarksJson,
                    String.join(", ", payload.competencyNames())
            );
        } catch (Exception e) {
            return payload.instructions();
        }
    }
}
