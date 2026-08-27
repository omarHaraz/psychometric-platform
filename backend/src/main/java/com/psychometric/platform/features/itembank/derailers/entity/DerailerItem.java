package com.psychometric.platform.features.itembank.derailers.entity;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "derailer_items")
public class DerailerItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "statement_ar", nullable = false, columnDefinition = "TEXT")
    private String statementAr;

    @Column(name = "justification_ar", columnDefinition = "TEXT")
    private String justificationAr;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "derailer_item_types",
        joinColumns = @JoinColumn(name = "item_id"),
        inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private java.util.Set<DerailerType> derailerTypes = new java.util.HashSet<>();

    @Column(name = "ideal_target", nullable = false)
    private Integer idealTarget = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_scale_type", nullable = false, length = 30)
    private ResponseScaleType responseScaleType = ResponseScaleType.FREQUENCY;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_mode", nullable = false, length = 20)
    private ExamMode examMode = ExamMode.BOTH;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "exposure_count", nullable = false)
    private int exposureCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public DerailerItem() {
    }

    public DerailerItem(String statementAr, String justificationAr, java.util.Set<DerailerType> derailerTypes, Integer idealTarget, ResponseScaleType responseScaleType, ExamMode examMode) {
        this.statementAr = statementAr;
        this.justificationAr = justificationAr;
        this.derailerTypes = derailerTypes;
        this.idealTarget = idealTarget;
        this.responseScaleType = responseScaleType;
        this.examMode = examMode;
        this.active = true;
        this.exposureCount = 0;
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

    public java.util.Set<DerailerType> getDerailerTypes() {
        return derailerTypes;
    }

    public void setDerailerTypes(java.util.Set<DerailerType> derailerTypes) {
        this.derailerTypes = derailerTypes;
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
