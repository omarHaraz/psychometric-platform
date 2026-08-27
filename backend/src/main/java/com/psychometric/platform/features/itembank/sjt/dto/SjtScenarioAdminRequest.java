package com.psychometric.platform.features.itembank.sjt.dto;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.sjt.entity.SjtComplexity;
import com.psychometric.platform.features.itembank.sjt.entity.SjtOptionKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SjtScenarioAdminRequest {

    @NotBlank(message = "رمز السيناريو مطلوب")
    private String itemCode;

    @NotNull(message = "معرف المجال مطلوب")
    private Long domainId;

    @NotBlank(message = "عنوان السيناريو مطلوب")
    private String titleAr;

    @NotBlank(message = "نص السيناريو مطلوب")
    private String narrativeAr;

    private String scenarioImageUrl;

    @NotNull(message = "مستوى التعقيد مطلوب")
    private SjtComplexity complexity = SjtComplexity.DIRECT;

    @NotNull(message = "مفتاح الإجراء الأفضل مطلوب")
    private SjtOptionKey bestOptionKey;

    private String rationaleAr;
    private String commonMistakeAr;
    private String coachingNoteAr;

    @NotNull(message = "نمط الاختبار مطلوب")
    private ExamMode examMode = ExamMode.BOTH;

    private List<SjtOptionAdminDto> options = new ArrayList<>();

    public SjtScenarioAdminRequest() {
    }

    public SjtScenarioAdminRequest(String itemCode, Long domainId, String titleAr, String narrativeAr,
                                  String scenarioImageUrl, SjtComplexity complexity, SjtOptionKey bestOptionKey,
                                  String rationaleAr, String commonMistakeAr, String coachingNoteAr,
                                  ExamMode examMode, List<SjtOptionAdminDto> options) {
        this.itemCode = itemCode;
        this.domainId = domainId;
        this.titleAr = titleAr;
        this.narrativeAr = narrativeAr;
        this.scenarioImageUrl = scenarioImageUrl;
        this.complexity = complexity;
        this.bestOptionKey = bestOptionKey;
        this.rationaleAr = rationaleAr;
        this.commonMistakeAr = commonMistakeAr;
        this.coachingNoteAr = coachingNoteAr;
        this.examMode = examMode;
        this.options = options != null ? options : new ArrayList<>();
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getNarrativeAr() {
        return narrativeAr;
    }

    public void setNarrativeAr(String narrativeAr) {
        this.narrativeAr = narrativeAr;
    }

    public String getScenarioImageUrl() {
        return scenarioImageUrl;
    }

    public void setScenarioImageUrl(String scenarioImageUrl) {
        this.scenarioImageUrl = scenarioImageUrl;
    }

    public SjtComplexity getComplexity() {
        return complexity;
    }

    public void setComplexity(SjtComplexity complexity) {
        this.complexity = complexity;
    }

    public SjtOptionKey getBestOptionKey() {
        return bestOptionKey;
    }

    public void setBestOptionKey(SjtOptionKey bestOptionKey) {
        this.bestOptionKey = bestOptionKey;
    }

    public String getRationaleAr() {
        return rationaleAr;
    }

    public void setRationaleAr(String rationaleAr) {
        this.rationaleAr = rationaleAr;
    }

    public String getCommonMistakeAr() {
        return commonMistakeAr;
    }

    public void setCommonMistakeAr(String commonMistakeAr) {
        this.commonMistakeAr = commonMistakeAr;
    }

    public String getCoachingNoteAr() {
        return coachingNoteAr;
    }

    public void setCoachingNoteAr(String coachingNoteAr) {
        this.coachingNoteAr = coachingNoteAr;
    }

    public ExamMode getExamMode() {
        return examMode;
    }

    public void setExamMode(ExamMode examMode) {
        this.examMode = examMode;
    }

    public List<SjtOptionAdminDto> getOptions() {
        return options;
    }

    public void setOptions(List<SjtOptionAdminDto> options) {
        this.options = options;
    }
}
