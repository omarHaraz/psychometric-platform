package com.psychometric.platform.features.itembank.personality.entity;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "personality_items")
public class PersonalityItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "statement_ar", nullable = false, columnDefinition = "TEXT")
    private String statementAr;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "personality_item_competencies",
        joinColumns = @JoinColumn(name = "item_id"),
        inverseJoinColumns = @JoinColumn(name = "competency_id")
    )
    private Set<Competency> competencies = new HashSet<>();

    @Column(name = "ideal_target", nullable = false)
    private Integer idealTarget = 5;

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

    @Column(name = "justification_ar", columnDefinition = "TEXT")
    private String justificationAr;

    public PersonalityItem() {
    }

    public PersonalityItem(String statementAr, Set<Competency> competencies, Integer idealTarget, ExamMode examMode) {
        this.statementAr = statementAr;
        this.competencies = competencies;
        this.idealTarget = idealTarget;
        this.examMode = examMode;
        this.active = true;
        this.exposureCount = 0;
    }

    public PersonalityItem(String statementAr, Set<Competency> competencies, Integer idealTarget, ExamMode examMode, String justificationAr) {
        this.statementAr = statementAr;
        this.competencies = competencies;
        this.idealTarget = idealTarget;
        this.examMode = examMode;
        this.justificationAr = justificationAr;
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

    public Set<Competency> getCompetencies() {
        return competencies;
    }

    public void setCompetencies(Set<Competency> competencies) {
        this.competencies = competencies;
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
