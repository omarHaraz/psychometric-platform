package com.psychometric.platform.features.itembank;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerItemResponse;
import com.psychometric.platform.features.itembank.derailers.entity.DerailerItem;
import com.psychometric.platform.features.itembank.derailers.entity.DerailerType;
import com.psychometric.platform.features.itembank.derailers.entity.ResponseScaleType;
import com.psychometric.platform.features.itembank.gcat.dto.GcatOptionCandidateDto;
import com.psychometric.platform.features.itembank.gcat.dto.GcatQuestionCandidateDto;
import com.psychometric.platform.features.itembank.gcat.entity.*;
import com.psychometric.platform.features.itembank.personality.dto.PersonalityItemResponse;
import com.psychometric.platform.features.itembank.personality.entity.Competency;
import com.psychometric.platform.features.itembank.personality.entity.PersonalityItem;
import com.psychometric.platform.features.itembank.sjt.dto.SjtOptionResponse;
import com.psychometric.platform.features.itembank.sjt.dto.SjtScenarioResponse;
import com.psychometric.platform.features.itembank.sjt.entity.SjtComplexity;
import com.psychometric.platform.features.itembank.sjt.entity.SjtDomain;
import com.psychometric.platform.features.itembank.sjt.entity.SjtOption;
import com.psychometric.platform.features.itembank.sjt.entity.SjtOptionKey;
import com.psychometric.platform.features.itembank.sjt.entity.SjtScenario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemBankValidationPassTest {

    @Test
    @DisplayName("Validation Pass: Verify all 4 Item Bank Entity Structures & Mandatory Fields")
    void testAllFourItemTypes_EntityIntegrity() {
        // 1. PersonalityItem Entity Integrity
        Competency comp = new Competency("COMP_1", "الكفاءة الأولى", "تعريف", 1);
        comp.setId(1L);
        PersonalityItem pItem = new PersonalityItem("نص العبارة", comp, 5, ExamMode.BOTH);
        pItem.setId(10L);

        assertThat(pItem.getId()).isEqualTo(10L);
        assertThat(pItem.getStatementAr()).isEqualTo("نص العبارة");
        assertThat(pItem.getCompetency().getCode()).isEqualTo("COMP_1");
        assertThat(pItem.getIdealTarget()).isEqualTo(5);
        assertThat(pItem.getExamMode()).isEqualTo(ExamMode.BOTH);
        assertThat(pItem.isActive()).isTrue();

        // 2. DerailerItem Entity Integrity
        DerailerType dType = new DerailerType("نمط التعطيل الأول", "تعريف");
        dType.setId(2L);
        DerailerItem dItem = new DerailerItem("نص التعطيل", dType, 1, ResponseScaleType.FREQUENCY, ExamMode.FULL);
        dItem.setId(20L);

        assertThat(dItem.getId()).isEqualTo(20L);
        assertThat(dItem.getStatementAr()).isEqualTo("نص التعطيل");
        assertThat(dItem.getDerailerType().getNameAr()).isEqualTo("نمط التعطيل الأول");
        assertThat(dItem.getIdealTarget()).isEqualTo(1);
        assertThat(dItem.getResponseScaleType()).isEqualTo(ResponseScaleType.FREQUENCY);
        assertThat(dItem.getExamMode()).isEqualTo(ExamMode.FULL);
        assertThat(dItem.isActive()).isTrue();

        // 3. Cognitive (GCAT) Entity Integrity
        GcatSubtest gSubtest = new GcatSubtest(GcatSubtestCode.ABSTRACT, "الاستدلال التجريدي", "الوصف", 14, 7, 1200);
        gSubtest.setId(3L);
        GcatQuestion gQuest = new GcatQuestion(
                "GCAT-ABS-01", gSubtest, "مصفوفة 1", "توجيه", "https://cdn.image.png",
                "نمط التدوير", "ملاحظة", "قاعدة", "تطبيق",
                GcatOptionKey.B, GcatDifficulty.MEDIUM, ExamMode.BOTH
        );
        gQuest.setId(30L);
        GcatOption gOptA = new GcatOption(gQuest, GcatOptionKey.A, "خيار أ", null, false);
        GcatOption gOptB = new GcatOption(gQuest, GcatOptionKey.B, "خيار ب", null, true);
        gQuest.getOptions().addAll(List.of(gOptA, gOptB));

        assertThat(gQuest.getId()).isEqualTo(30L);
        assertThat(gQuest.getItemCode()).isEqualTo("GCAT-ABS-01");
        assertThat(gQuest.getSubtest().getCode()).isEqualTo(GcatSubtestCode.ABSTRACT);
        assertThat(gQuest.getCorrectOptionKey()).isEqualTo(GcatOptionKey.B);
        assertThat(gQuest.getOptions()).hasSize(2);
        assertThat(gQuest.getOptions().stream().filter(GcatOption::isCorrect).count()).isEqualTo(1);

        // 4. SJT Scenario Entity Integrity
        SjtDomain sDomain = new SjtDomain("DECISION_INTEGRITY", "اتخاذ القرارات", "الوصف", 1);
        sDomain.setId(4L);
        SjtScenario sScen = new SjtScenario(
                "SJT-DEC-01", sDomain, "سيناريو 1", "نص السيناريو", null,
                SjtComplexity.DIRECT, SjtOptionKey.A, "التعليل", "الخطأ", "ملاحظة", ExamMode.BOTH
        );
        sScen.setId(40L);
        SjtOption sOptA = new SjtOption(sScen, SjtOptionKey.A, "إجراء أ", 1.0, "مثالي", true);
        SjtOption sOptB = new SjtOption(sScen, SjtOptionKey.B, "إجراء ب", 0.0, "سيء", false);
        sScen.getOptions().addAll(List.of(sOptA, sOptB));

        assertThat(sScen.getId()).isEqualTo(40L);
        assertThat(sScen.getItemCode()).isEqualTo("SJT-DEC-01");
        assertThat(sScen.getDomain().getCode()).isEqualTo("DECISION_INTEGRITY");
        assertThat(sScen.getBestOptionKey()).isEqualTo(SjtOptionKey.A);
        assertThat(sScen.getOptions()).hasSize(2);
        assertThat(sScen.getOptions().stream().filter(SjtOption::isBestAction).count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Zero-Knowledge Security Audit: All 4 Candidate DTOs strictly prevent key leakage")
    void testZeroKnowledgeSecurityAudit_CandidateDTOPerimeter() {
        // 1. PersonalityItemResponse
        List<String> pFields = Arrays.stream(PersonalityItemResponse.class.getDeclaredFields()).map(Field::getName).toList();
        assertThat(pFields).doesNotContain("idealTarget", "target", "scoringKey", "weight");

        // 2. DerailerItemResponse
        List<String> dFields = Arrays.stream(DerailerItemResponse.class.getDeclaredFields()).map(Field::getName).toList();
        assertThat(dFields).doesNotContain("idealTarget", "target", "riskThreshold");

        // 3. GcatQuestionCandidateDto & GcatOptionCandidateDto
        List<String> gqFields = Arrays.stream(GcatQuestionCandidateDto.class.getDeclaredFields()).map(Field::getName).toList();
        assertThat(gqFields).doesNotContain("correctOptionKey", "observationAr", "ruleAr", "applicationAr");

        List<String> goFields = Arrays.stream(GcatOptionCandidateDto.class.getDeclaredFields()).map(Field::getName).toList();
        assertThat(goFields).doesNotContain("isCorrect", "correct");

        // 4. SjtScenarioResponse & SjtOptionResponse
        List<String> sqFields = Arrays.stream(SjtScenarioResponse.class.getDeclaredFields()).map(Field::getName).toList();
        assertThat(sqFields).doesNotContain("bestOptionKey", "rationaleAr", "commonMistakeAr", "coachingNoteAr");

        List<String> soFields = Arrays.stream(SjtOptionResponse.class.getDeclaredFields()).map(Field::getName).toList();
        assertThat(soFields).doesNotContain("effectivenessScore", "scoringRationaleAr", "bestAction", "isBestAction");
    }
}
