package com.psychometric.platform.features.itembank.personality.dto;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;

import java.time.Instant;
import java.util.List;

public class PersonalityItemAdminResponse {
    private Long id;
    private String statementAr;
    private List<Long> competencyIds;
    private List<String> competencyNamesAr;
    private Integer idealTarget;
    private ExamMode examMode;
    private boolean active;
    private int exposureCount;
    private Instant createdAt;
    private String justificationAr;

    public PersonalityItemAdminResponse() {
    }

    public PersonalityItemAdminResponse(Long id, String statementAr, List<Long> competencyIds, List<String> competencyNamesAr,
                                        Integer idealTarget, ExamMode examMode, boolean active,
                                        int exposureCount, Instant createdAt, String justificationAr) {
        this.id = id;
        this.statementAr = statementAr;
        this.competencyIds = competencyIds;
        this.competencyNamesAr = competencyNamesAr;
        this.idealTarget = idealTarget;
        this.examMode = examMode;
        this.active = active;
        this.exposureCount = exposureCount;
        this.createdAt = createdAt;
        this.justificationAr = justificationAr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<String> getCompetencyNamesAr() {
        return competencyNamesAr;
    }

    public void setCompetencyNamesAr(List<String> competencyNamesAr) {
        this.competencyNamesAr = competencyNamesAr;
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

    public String getJustificationAr() {
        return justificationAr;
    }

    public void setJustificationAr(String justificationAr) {
        this.justificationAr = justificationAr;
    }
}
