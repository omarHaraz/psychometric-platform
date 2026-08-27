package com.psychometric.platform.features.itembank.derailers.dto;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.derailers.entity.ResponseScaleType;

import java.time.Instant;

public class DerailerItemAdminResponse {
    private Long id;
    private String statementAr;
    private Long derailerTypeId;
    private String derailerTypeNameAr;
    private Integer idealTarget;
    private ResponseScaleType responseScaleType;
    private ExamMode examMode;
    private boolean active;
    private int exposureCount;
    private Instant createdAt;

    public DerailerItemAdminResponse() {
    }

    public DerailerItemAdminResponse(Long id, String statementAr, Long derailerTypeId, String derailerTypeNameAr,
                                     Integer idealTarget, ResponseScaleType responseScaleType, ExamMode examMode,
                                     boolean active, int exposureCount, Instant createdAt) {
        this.id = id;
        this.statementAr = statementAr;
        this.derailerTypeId = derailerTypeId;
        this.derailerTypeNameAr = derailerTypeNameAr;
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

    public Long getDerailerTypeId() {
        return derailerTypeId;
    }

    public void setDerailerTypeId(Long derailerTypeId) {
        this.derailerTypeId = derailerTypeId;
    }

    public String getDerailerTypeNameAr() {
        return derailerTypeNameAr;
    }

    public void setDerailerTypeNameAr(String derailerTypeNameAr) {
        this.derailerTypeNameAr = derailerTypeNameAr;
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
