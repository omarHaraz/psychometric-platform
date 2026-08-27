package com.psychometric.platform.features.itembank;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.sjt.dto.SjtDomainResponse;
import com.psychometric.platform.features.itembank.sjt.dto.SjtOptionResponse;
import com.psychometric.platform.features.itembank.sjt.dto.SjtScenarioResponse;
import com.psychometric.platform.features.itembank.sjt.entity.SjtComplexity;
import com.psychometric.platform.features.itembank.sjt.entity.SjtDomain;
import com.psychometric.platform.features.itembank.sjt.entity.SjtOption;
import com.psychometric.platform.features.itembank.sjt.entity.SjtOptionKey;
import com.psychometric.platform.features.itembank.sjt.entity.SjtScenario;
import com.psychometric.platform.features.itembank.sjt.repository.SjtDomainRepository;
import com.psychometric.platform.features.itembank.sjt.repository.SjtScenarioRepository;
import com.psychometric.platform.features.itembank.sjt.service.SjtService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SjtServiceTest {

    @Mock
    private SjtDomainRepository sjtDomainRepository;

    @Mock
    private SjtScenarioRepository sjtScenarioRepository;

    private SjtService sjtService;

    private SjtDomain domain;
    private SjtScenario scenario;

    @BeforeEach
    void setUp() {
        sjtService = new SjtService(sjtDomainRepository, sjtScenarioRepository);

        domain = new SjtDomain("DECISION_INTEGRITY", "اتخاذ القرار", "الوصف", 1);
        domain.setId(1L);

        scenario = new SjtScenario(
                "SJT-DEC-01", domain, "عنوان", "نص السيناريو",
                null, SjtComplexity.TRADE_OFF, SjtOptionKey.A,
                "التعليل", "الخطأ الشائع", "الملاحظة الإرشادية", ExamMode.FULL
        );
        scenario.setId(100L);

        SjtOption optA = new SjtOption(scenario, SjtOptionKey.A, "خيار أ", 1.0, "فعال", true);
        optA.setId(10L);
        SjtOption optB = new SjtOption(scenario, SjtOptionKey.B, "خيار ب", 0.5, "ضعيف", false);
        optB.setId(20L);

        scenario.setOptions(List.of(optA, optB));
    }

    @Test
    @DisplayName("getAllDomains returns mapped SJT domain list")
    void testGetAllDomains() {
        when(sjtDomainRepository.findAllByOrderByDisplayOrderAsc()).thenReturn(List.of(domain));

        List<SjtDomainResponse> responses = sjtService.getAllDomains();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).code()).isEqualTo("DECISION_INTEGRITY");
        assertThat(responses.get(0).nameAr()).isEqualTo("اتخاذ القرار");
    }

    @Test
    @DisplayName("getCandidateScenarios returns candidate scenarios")
    void testGetCandidateScenarios() {
        when(sjtScenarioRepository.findActiveByExamModeWithOptions(anyList()))
                .thenReturn(List.of(scenario));

        List<SjtScenarioResponse> responses = sjtService.getCandidateScenarios(null, ExamMode.FULL);

        assertThat(responses).hasSize(1);
        SjtScenarioResponse res = responses.get(0);
        assertThat(res.id()).isEqualTo(100L);
        assertThat(res.itemCode()).isEqualTo("SJT-DEC-01");
        assertThat(res.domainId()).isEqualTo(1L);
        assertThat(res.complexity()).isEqualTo(SjtComplexity.TRADE_OFF);
        assertThat(res.options()).hasSize(2);
        assertThat(res.options().get(0).optionKey()).isEqualTo(SjtOptionKey.A);
    }

    @Test
    @DisplayName("getCandidateScenarios with domainId retrieves domain-filtered scenarios")
    void testGetCandidateScenariosWithDomain() {
        when(sjtScenarioRepository.findActiveByDomainAndExamModeWithOptions(eq(1L), anyList()))
                .thenReturn(List.of(scenario));

        List<SjtScenarioResponse> responses = sjtService.getCandidateScenarios(1L, ExamMode.FULL);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).itemCode()).isEqualTo("SJT-DEC-01");
    }

    @Test
    @DisplayName("Security & Anti-Tampering: SjtScenarioResponse and SjtOptionResponse strictly exclude scoring rubrics")
    void testSecurityRule_ZeroKnowledgeDTOPerimeter() {
        Field[] scenarioFields = SjtScenarioResponse.class.getDeclaredFields();
        List<String> scenarioFieldNames = Arrays.stream(scenarioFields).map(Field::getName).toList();

        assertThat(scenarioFieldNames).contains(
                "id", "itemCode", "domainId", "titleAr", "narrativeAr", "complexity", "options"
        );
        assertThat(scenarioFieldNames).doesNotContain(
                "bestOptionKey", "rationaleAr", "commonMistakeAr", "coachingNoteAr", "effectivenessScore"
        );

        Field[] optionFields = SjtOptionResponse.class.getDeclaredFields();
        List<String> optionFieldNames = Arrays.stream(optionFields).map(Field::getName).toList();

        assertThat(optionFieldNames).contains(
                "id", "optionKey", "actionTextAr"
        );
        assertThat(optionFieldNames).doesNotContain(
                "effectivenessScore", "scoringRationaleAr", "isBestAction"
        );
    }
}
