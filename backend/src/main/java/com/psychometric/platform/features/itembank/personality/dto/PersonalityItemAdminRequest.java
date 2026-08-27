package com.psychometric.platform.features.itembank.personality.dto;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class PersonalityItemAdminRequest {

    @NotBlank(message = "نص العبارة مطلوب")
    private String statementAr;

    @NotNull(message = "معرفات الكفاءات مطلوبة")
    private List<Long> competencyIds;

    @NotNull(message = "المستوى المستهدف المثالي مطلوب")
    @Min(value = 1, message = "المستوى المستهدف يجب أن يكون بين 1 و 5")
    @Max(value = 5, message = "المستوى المستهدف يجب أن يكون بين 1 و 5")
    private Integer idealTarget = 5;

    @NotNull(message = "نمط الاختبار مطلوب")
    private ExamMode examMode = ExamMode.BOTH;

    private String justificationAr;

    public PersonalityItemAdminRequest() {
    }

    public PersonalityItemAdminRequest(String statementAr, List<Long> competencyIds, Integer idealTarget, ExamMode examMode, String justificationAr) {
        this.statementAr = statementAr;
        this.competencyIds = competencyIds;
        this.idealTarget = idealTarget;
        this.examMode = examMode;
        this.justificationAr = justificationAr;
    }

    public String getStatementAr() {
        return statementAr;
    }

    public void setStatementAr(String statementAr) {
        this.statementAr = statementAr;
    }

    public List<Long> getCompetencyIds() {
        return competencyIds;
    }

    public void setCompetencyIds(List<Long> competencyIds) {
        this.competencyIds = competencyIds;
    }

    public Integer getIdealTarget() {
        return idealTarget;
    }

    public void setIdealTarget(Integer idealTarget) {
        this.idealTarget = idealTarget;
    }

    public ExamMode getExamMode() {
        return examMode;
    }

    public void setExamMode(ExamMode examMode) {
        this.examMode = examMode;
    }

    public String getJustificationAr() {
        return justificationAr;
    }

    public void setJustificationAr(String justificationAr) {
        this.justificationAr = justificationAr;
    }
}
