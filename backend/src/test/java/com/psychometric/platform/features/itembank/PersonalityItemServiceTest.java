package com.psychometric.platform.features.itembank;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.personality.dto.PersonalityItemResponse;
import com.psychometric.platform.features.itembank.personality.entity.Competency;
import com.psychometric.platform.features.itembank.personality.entity.PersonalityItem;
import com.psychometric.platform.features.itembank.personality.repository.PersonalityItemRepository;
import com.psychometric.platform.features.itembank.personality.service.PersonalityItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonalityItemServiceTest {

    @Mock
    private PersonalityItemRepository personalityItemRepository;

    private PersonalityItemService personalityItemService;

    private Competency testCompetency;
    private PersonalityItem itemBoth;
    private PersonalityItem itemQuick;
    private PersonalityItem itemFull;

    @BeforeEach
    void setUp() {
        personalityItemService = new PersonalityItemService(personalityItemRepository);

        testCompetency = new Competency("COMMUNICATION", "التواصل", "تعريف", 1);
        testCompetency.setId(101L);

        itemBoth = new PersonalityItem("عبارة 1", testCompetency, 5, ExamMode.BOTH);
        itemBoth.setId(1L);

        itemQuick = new PersonalityItem("عبارة 2", testCompetency, 1, ExamMode.QUICK);
        itemQuick.setId(2L);

        itemFull = new PersonalityItem("عبارة 3", testCompetency, 1, ExamMode.FULL);
        itemFull.setId(3L);
    }

    @Test
    @DisplayName("QUICK mode queries items with ExamMode QUICK and BOTH")
    void testGetPersonalityItems_QuickMode() {
        when(personalityItemRepository.findByExamModeInAndActiveTrue(eq(List.of(ExamMode.QUICK, ExamMode.BOTH))))
                .thenReturn(List.of(itemBoth, itemQuick));

        List<PersonalityItemResponse> responses = personalityItemService.getPersonalityItems(ExamMode.QUICK);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).id()).isEqualTo(1L);
        assertThat(responses.get(0).statementAr()).isEqualTo("عبارة 1");
        assertThat(responses.get(0).competencyId()).isEqualTo(101L);

        assertThat(responses.get(1).id()).isEqualTo(2L);
        assertThat(responses.get(1).statementAr()).isEqualTo("عبارة 2");
        assertThat(responses.get(1).competencyId()).isEqualTo(101L);

        verify(personalityItemRepository).findByExamModeInAndActiveTrue(List.of(ExamMode.QUICK, ExamMode.BOTH));
    }

    @Test
    @DisplayName("FULL mode queries items with ExamMode FULL and BOTH")
    void testGetPersonalityItems_FullMode() {
        when(personalityItemRepository.findByExamModeInAndActiveTrue(eq(List.of(ExamMode.FULL, ExamMode.BOTH))))
                .thenReturn(List.of(itemBoth, itemFull));

        List<PersonalityItemResponse> responses = personalityItemService.getPersonalityItems(ExamMode.FULL);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).id()).isEqualTo(1L);
        assertThat(responses.get(1).id()).isEqualTo(3L);

        verify(personalityItemRepository).findByExamModeInAndActiveTrue(List.of(ExamMode.FULL, ExamMode.BOTH));
    }

    @Test
    @DisplayName("BOTH mode queries items strictly with ExamMode BOTH")
    void testGetPersonalityItems_BothMode() {
        when(personalityItemRepository.findByExamModeInAndActiveTrue(eq(List.of(ExamMode.BOTH))))
                .thenReturn(List.of(itemBoth));

        List<PersonalityItemResponse> responses = personalityItemService.getPersonalityItems(ExamMode.BOTH);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).id()).isEqualTo(1L);

        verify(personalityItemRepository).findByExamModeInAndActiveTrue(List.of(ExamMode.BOTH));
    }

    @Test
    @DisplayName("Default mode (null) falls back to QUICK mode query")
    void testGetPersonalityItems_NullModeDefaultsToQuick() {
        when(personalityItemRepository.findByExamModeInAndActiveTrue(eq(List.of(ExamMode.QUICK, ExamMode.BOTH))))
                .thenReturn(List.of(itemBoth, itemQuick));

        List<PersonalityItemResponse> responses = personalityItemService.getPersonalityItems(null);

        assertThat(responses).hasSize(2);
        verify(personalityItemRepository).findByExamModeInAndActiveTrue(List.of(ExamMode.QUICK, ExamMode.BOTH));
    }

    @Test
    @DisplayName("Empty personality items returns an empty list without error")
    void testGetPersonalityItems_EmptyList() {
        when(personalityItemRepository.findByExamModeInAndActiveTrue(eq(List.of(ExamMode.QUICK, ExamMode.BOTH))))
                .thenReturn(List.of());

        List<PersonalityItemResponse> responses = personalityItemService.getPersonalityItems(ExamMode.QUICK);

        assertThat(responses).isNotNull().isEmpty();
        verify(personalityItemRepository).findByExamModeInAndActiveTrue(List.of(ExamMode.QUICK, ExamMode.BOTH));
    }

    @Test
    @DisplayName("Security & Anti-Tampering: PersonalityItemResponse DTO has NO scoring key fields")
    void testSecurityRule_ZeroKnowledgeDTOPerimeter() {
        Field[] declaredFields = PersonalityItemResponse.class.getDeclaredFields();
        List<String> fieldNames = Arrays.stream(declaredFields).map(Field::getName).toList();

        assertThat(fieldNames).containsExactlyInAnyOrder("id", "statementAr", "competencyId");
        assertThat(fieldNames).doesNotContain("idealTarget", "target", "answerKey", "score");
    }
}
