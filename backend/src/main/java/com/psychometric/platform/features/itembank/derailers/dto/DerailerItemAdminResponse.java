package com.psychometric.platform.features.itembank.derailers.dto;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.derailers.entity.ResponseScaleType;

import java.time.Instant;

public class DerailerItemAdminResponse {
    private Long id;
    private String statementAr;
    private String justificationAr;
    private java.util.List<Long> derailerTypeIds;
    private java.util.List<String> derailerTypeNamesAr;
    private Integer idealTarget;
    private ResponseScaleType responseScaleType;
    private ExamMode examMode;
    private boolean active;
    private int exposureCount;
    private Instant createdAt;

    public DerailerItemAdminResponse() {
    }

    public DerailerItemAdminResponse(Long id, String statementAr, String justificationAr, java.util.List<Long> derailerTypeIds, java.util.List<String> derailerTypeNamesAr,
                                     Integer idealTarget, ResponseScaleType responseScaleType, ExamMode examMode,
                                     boolean active, int exposureCount, Instant createdAt) {
        this.id = id;
        this.statementAr = statementAr;
        this.justificationAr = justificationAr;
        this.derailerTypeIds = derailerTypeIds;
        this.derailerTypeNamesAr = derailerTypeNamesAr;
        this.idealTarget = idealTarget;
        this.responseScaleType = responseScaleType;
        this.examMode = examMode;
        this.active = active;
        this.exposureCount = exposureCount;
        this.createdAt = createdAt;
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

    public String getJustificationAr() {
        return justificationAr;
    }

    public void setJustificationAr(String justificationAr) {
        this.justificationAr = justificationAr;
    }

    public java.util.List<Long> getDerailerTypeIds() {
        return derailerTypeIds;
    }

    public void setDerailerTypeIds(java.util.List<Long> derailerTypeIds) {
        this.derailerTypeIds = derailerTypeIds;
    }

    public java.util.List<String> getDerailerTypeNamesAr() {
        return derailerTypeNamesAr;
    }

    public void setDerailerTypeNamesAr(java.util.List<String> derailerTypeNamesAr) {
        this.derailerTypeNamesAr = derailerTypeNamesAr;
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
}
