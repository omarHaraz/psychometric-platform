package com.psychometric.platform.features.report.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.features.report.service.LeadershipReportGeneratorService.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * GeminiAiReportClient
 *
 * Implements the {@link AiReportClient} interface to call Google Gemini API using Spring RestClient.
 * Supports targeted, modular calls for each section of the report with exact candidate context profiles.
 */
@Component
@Primary
public class GeminiAiReportClient implements AiReportClient {

    private static final Logger log = LoggerFactory.getLogger(GeminiAiReportClient.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String geminiApiUrl;
    private final String geminiApiKey;
    private final DefaultMockAiClient mockFallback = new DefaultMockAiClient();

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

    // =========================================================================
    // MODULAR SECTION CALLS (Candidate Context Profiles)
    // =========================================================================

    @Cacheable(value = "aiImpressionCache", key = "#payload.socialScore() + '-' + #payload.socialRisk() + '-' + #payload.centralScore() + '-' + #payload.centralRisk()")
    @Override
    public ImpressionResponseDto generateImpressionNarratives(ImpressionPayload payload) {
        String prompt = String.format(Locale.US,
                """
                أنت خبير تقييم قيادات. قم بتحليل استجابات المرشح في مقاييس إدارة الانطباعات بناءً على المعطيات التالية:
                مؤشر التظاهر الاجتماعي: الدرجة %d (خطر %s).
                مؤشر وسطية الإجابة: الدرجة %d (خطر %s).
                اكتب فقرة تفسيرية واحدة (Interpretation) لكل مؤشر تشرح سلوك المرشح في الاختبار باللغة العربية الفصحى. قم بإرجاع النتيجة بصيغة JSON حصراً بالصيغة التالية:
                {"socialInterpretation": "...", "centralInterpretation": "..."}
                """,
                payload.socialScore(), payload.socialRisk(),
                payload.centralScore(), payload.centralRisk()
        );

        return callGeminiForJson(prompt, ImpressionResponseDto.class, () -> mockFallback.generateImpressionNarratives(payload));
    }

    @Cacheable(value = "aiDerailersCache", key = "#payload.reserved() + '-' + #payload.emotionality() + '-' + #payload.hostility() + '-' + #payload.impulsivity() + '-' + #payload.rigidity() + '-' + #payload.unconventionality()")
    @Override
    public DerailersResponseDto generateDerailersNarratives(DerailersPayload payload) {
        String prompt = String.format(Locale.US,
                """
                قم بتحليل السمات الشخصية المعرقلة (Derailers) لهذا القائد بناءً على الدرجات التالية (من 1 إلى 10). اكتب فقرة تحليلية قصيرة لكل سمة تشرح كيف تؤثر على قيادته تحت الضغط:
                - التحفظ: %d/10
                - الانفعالية: %d/10
                - العدائية: %d/10
                - الاندفاعية: %d/10
                - الصرامة: %d/10
                - اللامألوفية: %d/10
                قم بإرجاع النتيجة بصيغة JSON حصراً بالصيغة التالية:
                {"reservedText": "...", "emotionalityText": "...", "hostilityText": "...", "impulsivityText": "...", "rigidityText": "...", "unconventionalityText": "..."}
                """,
                payload.reserved(), payload.emotionality(), payload.hostility(),
                payload.impulsivity(), payload.rigidity(), payload.unconventionality()
        );

        return callGeminiForJson(prompt, DerailersResponseDto.class, () -> mockFallback.generateDerailersNarratives(payload));
    }

    @Cacheable(value = "aiCompetencyPageCache", key = "#payload.pageNum() + '-' + #payload.score() + '-' + #payload.questionsAndAnswers()")
    @Override
    public CompetencyPageResponseDto generateCompetencyPageNarratives(CompetencyPagePayload payload) {
        StringBuilder subReqsText = new StringBuilder();
        for (int i = 0; i < payload.subIndicatorReqs().size(); i++) {
            subReqsText.append(payload.subIndicatorReqs().get(i)).append("\n\n");
        }

        String jsonSchema = payload.pageNum() == 11
                ? """
                {
                  "req1": "...", "result1": "...", "rec1": "...",
                  "req2": "...", "result2": "...", "rec2": "..."
                }
                """
                : """
                {
                  "req1": "...", "result1": "...", "rec1": "...",
                  "req2": "...", "result2": "...", "rec2": "...",
                  "req3": "...", "result3": "...", "rec3": "..."
                }
                """;

        String prompt = String.format(Locale.US,
                """
                أنت خبير في علم النفس التنظيمي وتقييم القيادات. اكتب بلغة إنسان طبيعية ولكن لغة أكاديمية سهلة وبسيطة.

                قم بتحليل أداء المرشح في كفاءة: %s.
                حصل المرشح على درجة إجمالية: %.2f من 5.0.

                فيما يلي الأسئلة التي أجاب عليها المرشح ضمن هذه الكفاءة وإجاباته المحددة:
                %s

                بناءً على إجابات المرشح الفعلية ودرجته، اكتب تحليلاً مفصلاً للمؤشرات الفرعية التالية:

                %s
                لكل مؤشر فرعي، قدم:
                req: متطلب الكفاءة (جملة واحدة).
                result: نتيجة المرشح (تحليل دقيق ومخصص يربط بين درجته وكيفية إجابته على الأسئلة المتعلقة بهذا المؤشر).
                rec: توصية تطويرية (خطوة عملية مخصصة لمعالجة الفجوات التي ظهرت في إجاباته).

                يجب أن يكون المخرج النهائي بصيغة JSON فقط متطابق تماماً مع هذا الهيكل:
                %s
                """,
                payload.competencyTitle(),
                payload.score(),
                payload.questionsAndAnswers() != null && !payload.questionsAndAnswers().isBlank()
                        ? payload.questionsAndAnswers()
                        : "لا توجد تفاصيل أسئلة إضافية.",
                subReqsText.toString().trim(),
                jsonSchema.trim()
        );

        return callGeminiForJson(prompt, CompetencyPageResponseDto.class, () -> mockFallback.generateCompetencyPageNarratives(payload));
    }

    @Cacheable(value = "aiGrowPlanCache", key = "#payload.candidateName() + '-' + #payload.topStrengths() + '-' + #payload.developmentAreas()")
    @Override
    public GrowPlanResponseDto generateGrowPlanNarratives(GrowPlanPayload payload) {
        String prompt = String.format(Locale.US,
                """
                أنت مستشار تنفيذي لتطوير القيادات. بناءً على نتائج تقييم القائد (%s):
                - أبرز نقاط القوة: %s
                - أبرز مجالات التطوير: %s
                - %s
                اكتب خطة تطوير قيادية متكاملة بنموذج GROW باللغة العربية الفصحى. أرجع JSON حصراً بالصيغة:
                {"growGoalText": "...", "growRealityText": "...", "growOptionsText": "...", "growWillText": "..."}
                """,
                payload.candidateName() != null ? payload.candidateName() : "المرشح",
                payload.topStrengths(),
                payload.developmentAreas(),
                payload.prominentDerailers() != null ? payload.prominentDerailers() : ""
        );

        return callGeminiForJson(prompt, GrowPlanResponseDto.class, () -> mockFallback.generateGrowPlanNarratives(payload));
    }

    // =========================================================================
    // GENERIC GEMINI JSON CLIENT CALLER & DIRECT API CALL
    // =========================================================================

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

    public <T> T callGeminiForJson(String promptText, Class<T> clazz, Supplier<T> fallbackSupplier) {
        if (geminiApiKey.isBlank()) {
            return fallbackSupplier.get();
        }

        try {
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
                    String extractedText = parts.get(0).path("text").asText().trim();
                    if (extractedText.startsWith("```json")) {
                        extractedText = extractedText.substring(7);
                    } else if (extractedText.startsWith("```")) {
                        extractedText = extractedText.substring(3);
                    }
                    if (extractedText.endsWith("```")) {
                        extractedText = extractedText.substring(0, extractedText.length() - 3);
                    }
                    extractedText = extractedText.trim();
                    return objectMapper.readValue(extractedText, clazz);
                }
            }
            throw new IllegalStateException("Unexpected Gemini API response structure");
        } catch (Exception e) {
            log.warn("Gemini API call failed for {} ({}). Using fallback.", clazz.getSimpleName(), e.getMessage());
            return fallbackSupplier.get();
        }
    }

    @Override
    public String generateNarrativesJson(AiPromptPayload payload) {
        return mockFallback.generateNarrativesJson(payload);
    }
}
