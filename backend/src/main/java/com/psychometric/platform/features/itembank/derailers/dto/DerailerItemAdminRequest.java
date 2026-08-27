package com.psychometric.platform.features.itembank.derailers.dto;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.derailers.entity.ResponseScaleType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DerailerItemAdminRequest {

    @NotBlank(message = "نص العبارة مطلوب")
    private String statementAr;

    @NotNull(message = "معرف نمط السلوك المعطل مطلوب")
    private Long derailerTypeId;

    @NotNull(message = "المستوى المستهدف المثالي مطلوب")
    @Min(value = 1, message = "المستوى المستهدف يجب أن يكون بين 1 و 5")
    @Max(value = 5, message = "المستوى المستهدف يجب أن يكون بين 1 و 5")
    private Integer idealTarget = 1;

    @NotNull(message = "نوع مقياس الاستجابة مطلوب")
    private ResponseScaleType responseScaleType = ResponseScaleType.FREQUENCY;

    @NotNull(message = "نمط الاختبار مطلوب")
    private ExamMode examMode = ExamMode.FULL;

    public DerailerItemAdminRequest() {
    }

    public DerailerItemAdminRequest(String statementAr, Long derailerTypeId, Integer idealTarget, ResponseScaleType responseScaleType, ExamMode examMode) {
        this.statementAr = statementAr;
        this.derailerTypeId = derailerTypeId;
        this.idealTarget = idealTarget;
        this.responseScaleType = responseScaleType;
        this.examMode = examMode;
    }

    public String getStatementAr() {
        return statementAr;
    }

    public void setStatementAr(String statementAr) {
        this.statementAr = statementAr;
    }

    public Long getDerailerTypeId() {
        return derailerTypeId;
    }

    public void setDerailerTypeId(Long derailerTypeId) {
        this.derailerTypeId = derailerTypeId;
    }

    public Integer getIdealTarget() {
        return idealTarget;
    }

    public void setIdealTarget(Integer idealTarget) {
        this.idealTarget = idealTarget;
    }

    public ResponseScaleType getResponseScaleType() {
        return responseScaleType;
    }

    public void setResponseScaleType(ResponseScaleType responseScaleType) {
        this.responseScaleType = responseScaleType;
    }

    public ExamMode getExamMode() {
        return examMode;
    }

    public void setExamMode(ExamMode examMode) {
        this.examMode = examMode;
    }
}
