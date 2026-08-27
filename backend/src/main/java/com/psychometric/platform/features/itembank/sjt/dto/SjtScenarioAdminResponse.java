package com.psychometric.platform.features.itembank.sjt.dto;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.sjt.entity.SjtComplexity;
import com.psychometric.platform.features.itembank.sjt.entity.SjtOptionKey;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SjtScenarioAdminResponse {
    private Long id;
    private String itemCode;
    private Long domainId;
    private String domainNameAr;
    private String titleAr;
    private String narrativeAr;
    private String scenarioImageUrl;
    private SjtComplexity complexity;
    private SjtOptionKey bestOptionKey;
    private String rationaleAr;
    private String commonMistakeAr;
    private String coachingNoteAr;
    private ExamMode examMode;
    private boolean active;
    private int exposureCount;
    private Instant createdAt;
    private List<SjtOptionAdminDto> options = new ArrayList<>();

    public SjtScenarioAdminResponse() {
    }

    public SjtScenarioAdminResponse(Long id, String itemCode, Long domainId, String domainNameAr,
                                    String titleAr, String narrativeAr, String scenarioImageUrl,
                                    SjtComplexity complexity, SjtOptionKey bestOptionKey,
                                    String rationaleAr, String commonMistakeAr, String coachingNoteAr,
                                    ExamMode examMode, boolean active, int exposureCount,
                                    Instant createdAt, List<SjtOptionAdminDto> options) {
        this.id = id;
        this.itemCode = itemCode;
        this.domainId = domainId;
        this.domainNameAr = domainNameAr;
        this.titleAr = titleAr;
        this.narrativeAr = narrativeAr;
        this.scenarioImageUrl = scenarioImageUrl;
        this.complexity = complexity;
        this.bestOptionKey = bestOptionKey;
        this.rationaleAr = rationaleAr;
        this.commonMistakeAr = commonMistakeAr;
        this.coachingNoteAr = coachingNoteAr;
        this.examMode = examMode;
        this.active = active;
        this.exposureCount = exposureCount;
        this.createdAt = createdAt;
        this.options = options != null ? options : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDomainNameAr() {
        return domainNameAr;
    }

    public void setDomainNameAr(String domainNameAr) {
        this.domainNameAr = domainNameAr;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getExposureCount() {
        return exposureCount;
    }

    public void setExposureCount(int exposureCount) {
        this.exposureCount = exposureCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<SjtOptionAdminDto> getOptions() {
        return options;
    }

    public void setOptions(List<SjtOptionAdminDto> options) {
        this.options = options;
    }
}
