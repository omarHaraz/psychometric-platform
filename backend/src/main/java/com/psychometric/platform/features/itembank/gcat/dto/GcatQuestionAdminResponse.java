package com.psychometric.platform.features.itembank.gcat.dto;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.gcat.entity.GcatDifficulty;
import com.psychometric.platform.features.itembank.gcat.entity.GcatOptionKey;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GcatQuestionAdminResponse {
    private Long id;
    private String itemCode;
    private GcatSubtestCode subtestCode;
    private String titleAr;
    private String promptTextAr;
    private String questionImageUrl;
    private String questionImagePublicId;
    private String patternTypeAr;
    private String observationAr;
    private String ruleAr;
    private String applicationAr;
    private GcatOptionKey correctOptionKey;
    private GcatDifficulty difficulty;
    private ExamMode examMode;
    private boolean active;
    private int exposureCount;
    private Instant createdAt;
    private List<GcatOptionAdminDto> options = new ArrayList<>();

    public GcatQuestionAdminResponse() {
    }

    public GcatQuestionAdminResponse(Long id, String itemCode, GcatSubtestCode subtestCode, String titleAr,
                                    String promptTextAr, String questionImageUrl, String questionImagePublicId,
                                    String patternTypeAr, String observationAr, String ruleAr,
                                    String applicationAr, GcatOptionKey correctOptionKey,
                                    GcatDifficulty difficulty, ExamMode examMode, boolean active,
                                    int exposureCount, Instant createdAt, List<GcatOptionAdminDto> options) {
        this.id = id;
        this.itemCode = itemCode;
        this.subtestCode = subtestCode;
        this.titleAr = titleAr;
        this.promptTextAr = promptTextAr;
        this.questionImageUrl = questionImageUrl;
        this.questionImagePublicId = questionImagePublicId;
        this.patternTypeAr = patternTypeAr;
        this.observationAr = observationAr;
        this.ruleAr = ruleAr;
        this.applicationAr = applicationAr;
        this.correctOptionKey = correctOptionKey;
        this.difficulty = difficulty;
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

    public GcatSubtestCode getSubtestCode() {
        return subtestCode;
    }

    public void setSubtestCode(GcatSubtestCode subtestCode) {
        this.subtestCode = subtestCode;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getPromptTextAr() {
        return promptTextAr;
    }

    public void setPromptTextAr(String promptTextAr) {
        this.promptTextAr = promptTextAr;
    }

    public String getQuestionImageUrl() {
        return questionImageUrl;
    }

    public void setQuestionImageUrl(String questionImageUrl) {
        this.questionImageUrl = questionImageUrl;
    }

    public String getQuestionImagePublicId() {
        return questionImagePublicId;
    }

    public void setQuestionImagePublicId(String questionImagePublicId) {
        this.questionImagePublicId = questionImagePublicId;
    }

    public String getPatternTypeAr() {
        return patternTypeAr;
    }

    public void setPatternTypeAr(String patternTypeAr) {
        this.patternTypeAr = patternTypeAr;
    }

    public String getObservationAr() {
        return observationAr;
    }

    public void setObservationAr(String observationAr) {
        this.observationAr = observationAr;
    }

    public String getRuleAr() {
        return ruleAr;
    }

    public void setRuleAr(String ruleAr) {
        this.ruleAr = ruleAr;
    }

    public String getApplicationAr() {
        return applicationAr;
    }

    public void setApplicationAr(String applicationAr) {
        this.applicationAr = applicationAr;
    }

    public GcatOptionKey getCorrectOptionKey() {
        return correctOptionKey;
    }

    public void setCorrectOptionKey(GcatOptionKey correctOptionKey) {
        this.correctOptionKey = correctOptionKey;
    }

    public GcatDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(GcatDifficulty difficulty) {
        this.difficulty = difficulty;
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

    public List<GcatOptionAdminDto> getOptions() {
        return options;
    }

    public void setOptions(List<GcatOptionAdminDto> options) {
        this.options = options;
    }
}
