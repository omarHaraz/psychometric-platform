package com.psychometric.platform.features.itembank;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.gcat.controller.GcatController;
import com.psychometric.platform.features.itembank.gcat.dto.GcatOptionCandidateDto;
import com.psychometric.platform.features.itembank.gcat.dto.GcatQuestionCandidateDto;
import com.psychometric.platform.features.itembank.gcat.dto.GcatSubtestResponse;
import com.psychometric.platform.features.itembank.gcat.entity.GcatDifficulty;
import com.psychometric.platform.features.itembank.gcat.entity.GcatOptionKey;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import com.psychometric.platform.features.itembank.gcat.service.GcatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GcatControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GcatService gcatService;

    @BeforeEach
    void setUp() {
        GcatController controller = new GcatController(gcatService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/items/gcat/questions returns candidate-sanitized GCAT questions without diagnostic keys")
    void testGetQuestions() throws Exception {
        GcatOptionCandidateDto optA = new GcatOptionCandidateDto(1L, GcatOptionKey.A, "الخيار A", null);
        GcatOptionCandidateDto optB = new GcatOptionCandidateDto(2L, GcatOptionKey.B, "الخيار B", null);

        GcatQuestionCandidateDto question = new GcatQuestionCandidateDto(
                10L, "GCAT-ABS-01", GcatSubtestCode.ABSTRACT,
                "دوران السهم بزاوية ثابتة", "اختر الشكل المناسب",
                "https://res.cloudinary.com/psychometric/image/upload/v1/gcat/abstract/gcat_abs_01.png",
                GcatDifficulty.EASY, List.of(optA, optB)
        );

        when(gcatService.getCandidateQuestions(eq(null), eq(ExamMode.FULL)))
                .thenReturn(List.of(question));

        mockMvc.perform(get("/api/items/gcat/questions")
                        .param("mode", "FULL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].itemCode").value("GCAT-ABS-01"))
                .andExpect(jsonPath("$[0].subtestCode").value("ABSTRACT"))
                .andExpect(jsonPath("$[0].titleAr").value("دوران السهم بزاوية ثابتة"))
                .andExpect(jsonPath("$[0].questionImageUrl").exists())
                .andExpect(jsonPath("$[0].options.length()").value(2))
                .andExpect(jsonPath("$[0].correctOptionKey").doesNotExist())
                .andExpect(jsonPath("$[0].observationAr").doesNotExist())
                .andExpect(jsonPath("$[0].ruleAr").doesNotExist())
                .andExpect(jsonPath("$[0].applicationAr").doesNotExist());
    }

    @Test
    @DisplayName("GET /api/items/gcat/subtests returns 3 subtests taxonomy")
    void testGetSubtests() throws Exception {
        GcatSubtestResponse subtest = new GcatSubtestResponse(
                1L, GcatSubtestCode.ABSTRACT, "الاستدلال التجريدي", "الوصف", 14, 7, 1200
        );

        when(gcatService.getAllSubtests()).thenReturn(List.of(subtest));

        mockMvc.perform(get("/api/items/gcat/subtests")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("ABSTRACT"))
                .andExpect(jsonPath("$[0].nameAr").value("الاستدلال التجريدي"))
                .andExpect(jsonPath("$[0].fullModeQuota").value(14))
                .andExpect(jsonPath("$[0].quickModeQuota").value(7));
    }

    @Test
    @DisplayName("GET /api/items/gcat/questions?subtest=ABSTRACT returns subtest-specific questions")
    void testGetQuestionsBySubtest() throws Exception {
        GcatQuestionCandidateDto question = new GcatQuestionCandidateDto(
                20L, "GCAT-ABS-02", GcatSubtestCode.ABSTRACT,
                "دورتان تعملان معاً", "اختر النمط",
                null, GcatDifficulty.EASY, List.of()
        );

        when(gcatService.getCandidateQuestions(eq(GcatSubtestCode.ABSTRACT), eq(ExamMode.FULL)))
                .thenReturn(List.of(question));

        mockMvc.perform(get("/api/items/gcat/questions")
                        .param("subtest", "ABSTRACT")
                        .param("mode", "FULL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].itemCode").value("GCAT-ABS-02"));
    }

    @Test
    @DisplayName("GET /api/items/gcat/questions/{id} returns single question by id")
    void testGetQuestionById() throws Exception {
        GcatQuestionCandidateDto question = new GcatQuestionCandidateDto(
                30L, "GCAT-ABS-03", GcatSubtestCode.ABSTRACT,
                "إضافة عنصر واحد في كل خطوة", "اختر النمط",
                null, GcatDifficulty.EASY, List.of()
        );

        when(gcatService.getQuestionById(eq(30L)))
                .thenReturn(question);

        mockMvc.perform(get("/api/items/gcat/questions/30")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.itemCode").value("GCAT-ABS-03"));
    }
}
