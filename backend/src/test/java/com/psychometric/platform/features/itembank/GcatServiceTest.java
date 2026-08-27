package com.psychometric.platform.features.itembank;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.gcat.dto.GcatOptionCandidateDto;
import com.psychometric.platform.features.itembank.gcat.dto.GcatQuestionCandidateDto;
import com.psychometric.platform.features.itembank.gcat.dto.GcatSubtestResponse;
import com.psychometric.platform.features.itembank.gcat.entity.GcatDifficulty;
import com.psychometric.platform.features.itembank.gcat.entity.GcatOption;
import com.psychometric.platform.features.itembank.gcat.entity.GcatOptionKey;
import com.psychometric.platform.features.itembank.gcat.entity.GcatQuestion;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtest;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import com.psychometric.platform.features.itembank.gcat.repository.GcatQuestionRepository;
import com.psychometric.platform.features.itembank.gcat.repository.GcatSubtestRepository;
import com.psychometric.platform.features.itembank.gcat.service.GcatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GcatServiceTest {

    @Mock
    private GcatSubtestRepository subtestRepository;

    @Mock
    private GcatQuestionRepository questionRepository;

    private GcatService gcatService;

    private GcatSubtest subtest;
    private GcatQuestion question;

    @BeforeEach
    void setUp() {
        gcatService = new GcatService(subtestRepository, questionRepository);

        subtest = new GcatSubtest(
                GcatSubtestCode.ABSTRACT,
                "الاستدلال التجريدي",
                "الوصف",
                14, 7, 720
        );
        subtest.setId(1L);

        question = new GcatQuestion(
                "GCAT-ABS-01", subtest, "دوران السهم بزاوية ثابتة",
                "اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.",
                "https://res.cloudinary.com/psychometric/image/upload/v1/gcat/abstract/gcat_abs_01.png",
                "الدوران المنتظم",
                "اتجاه السهم ينتقل من أعلى إلى يمين ثم أسفل ثم يسار.",
                "يدور السهم 90 درجة مع عقارب الساعة في كل خطوة.",
                "بعد اتجاه اليسار يعود السهم إلى أعلى؛ لذلك الخيار B.",
                GcatOptionKey.B,
                GcatDifficulty.EASY,
                ExamMode.BOTH
        );
        question.setId(100L);

        GcatOption optA = new GcatOption(question, GcatOptionKey.A, "الخيار A", null, false);
        optA.setId(10L);
        GcatOption optB = new GcatOption(question, GcatOptionKey.B, "الخيار B", null, true);
        optB.setId(20L);

        question.setOptions(List.of(optA, optB));
    }

    @Test
    @DisplayName("getAllSubtests returns mapped GCAT subtest dimension taxonomies")
    void testGetAllSubtests() {
        when(subtestRepository.findAll()).thenReturn(List.of(subtest));

        List<GcatSubtestResponse> responses = gcatService.getAllSubtests();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).code()).isEqualTo(GcatSubtestCode.ABSTRACT);
        assertThat(responses.get(0).nameAr()).isEqualTo("الاستدلال التجريدي");
        assertThat(responses.get(0).fullModeQuota()).isEqualTo(14);
        assertThat(responses.get(0).quickModeQuota()).isEqualTo(7);
    }

    @Test
    @DisplayName("getCandidateQuestions retrieves candidate questions")
    void testGetCandidateQuestions() {
        when(questionRepository.findActiveByExamModeWithOptionCandidates(anyList()))
                .thenReturn(List.of(question));

        List<GcatQuestionCandidateDto> responses = gcatService.getCandidateQuestions(null, ExamMode.FULL);

        assertThat(responses).hasSize(1);
        GcatQuestionCandidateDto dto = responses.get(0);
        assertThat(dto.id()).isEqualTo(100L);
        assertThat(dto.itemCode()).isEqualTo("GCAT-ABS-01");
        assertThat(dto.subtestCode()).isEqualTo(GcatSubtestCode.ABSTRACT);
        assertThat(dto.titleAr()).isEqualTo("دوران السهم بزاوية ثابتة");
        assertThat(dto.questionImageUrl()).contains("cloudinary");
        assertThat(dto.difficulty()).isEqualTo(GcatDifficulty.EASY);
        assertThat(dto.options()).hasSize(2);
        assertThat(dto.options().get(0).optionKey()).isEqualTo(GcatOptionKey.A);
    }

    @Test
    @DisplayName("getCandidateQuestions with subtestCode retrieves subtest-filtered questions")
    void testGetCandidateQuestionsWithSubtest() {
        when(questionRepository.findActiveBySubtestAndExamModeWithOptionCandidates(eq(GcatSubtestCode.ABSTRACT), anyList()))
                .thenReturn(List.of(question));

        List<GcatQuestionCandidateDto> responses = gcatService.getCandidateQuestions(GcatSubtestCode.ABSTRACT, ExamMode.FULL);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).itemCode()).isEqualTo("GCAT-ABS-01");
    }

    @Test
    @DisplayName("getQuestionById retrieves sanitized question by id")
    void testGetQuestionById() {
        when(questionRepository.findByIdWithOptions(eq(100L))).thenReturn(Optional.of(question));

        GcatQuestionCandidateDto dto = gcatService.getQuestionById(100L);

        assertThat(dto).isNotNull();
        assertThat(dto.itemCode()).isEqualTo("GCAT-ABS-01");
        assertThat(dto.titleAr()).isEqualTo("دوران السهم بزاوية ثابتة");
    }

    @Test
    @DisplayName("Strict Security Perimeter: GcatQuestionCandidateDto and GcatOptionCandidateDto strictly exclude scoring rubrics and answers")
    void testSecurityRule_ZeroKnowledgeDTOPerimeter() {
        Field[] questionFields = GcatQuestionCandidateDto.class.getDeclaredFields();
        List<String> questionFieldNames = Arrays.stream(questionFields).map(Field::getName).toList();

        assertThat(questionFieldNames).contains(
                "id", "itemCode", "subtestCode", "titleAr", "promptTextAr",
                "questionImageUrl", "difficulty", "options"
        );
        assertThat(questionFieldNames).doesNotContain(
                "correctOptionKey", "observationAr", "ruleAr", "applicationAr"
        );

        Field[] optionFields = GcatOptionCandidateDto.class.getDeclaredFields();
        List<String> optionFieldNames = Arrays.stream(optionFields).map(Field::getName).toList();

        assertThat(optionFieldNames).contains(
                "id", "optionKey", "optionTextAr", "optionImageUrl"
        );
        assertThat(optionFieldNames).doesNotContain(
                "isCorrect", "correct"
        );
    }
}
