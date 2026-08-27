package com.psychometric.platform.features.itembank.gcat.entity;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gcat_questions")
public class GcatQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_code", nullable = false, unique = true, length = 100)
    private String itemCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtest_id", nullable = false)
    private GcatSubtest subtest;

    @Column(name = "title_ar", nullable = false, length = 255)
    private String titleAr;

    @Column(name = "prompt_text_ar", columnDefinition = "TEXT")
    private String promptTextAr;

    @Column(name = "question_image_url", length = 500)
    private String questionImageUrl;

    @Column(name = "question_image_public_id", length = 255)
    private String questionImagePublicId;

    @Column(name = "pattern_type_ar", length = 255)
    private String patternTypeAr;

    @Column(name = "observation_ar", columnDefinition = "TEXT")
    private String observationAr;

    @Column(name = "rule_ar", columnDefinition = "TEXT")
    private String ruleAr;

    @Column(name = "application_ar", columnDefinition = "TEXT")
    private String applicationAr;

    @Enumerated(EnumType.STRING)
    @Column(name = "correct_option_key", nullable = false, length = 10)
    private GcatOptionKey correctOptionKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GcatDifficulty difficulty = GcatDifficulty.MEDIUM;

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

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GcatOption> options = new ArrayList<>();

    public GcatQuestion() {
    }

    public GcatQuestion(String itemCode, GcatSubtest subtest, String titleAr, String promptTextAr,
                        String questionImageUrl, String patternTypeAr, String observationAr,
                        String ruleAr, String applicationAr, GcatOptionKey correctOptionKey,
                        GcatDifficulty difficulty, ExamMode examMode) {
        this.itemCode = itemCode;
        this.subtest = subtest;
        this.titleAr = titleAr;
        this.promptTextAr = promptTextAr;
        this.questionImageUrl = questionImageUrl;
        this.patternTypeAr = patternTypeAr;
        this.observationAr = observationAr;
        this.ruleAr = ruleAr;
        this.applicationAr = applicationAr;
        this.correctOptionKey = correctOptionKey;
        this.difficulty = difficulty;
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

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public GcatSubtest getSubtest() {
        return subtest;
    }

    public void setSubtest(GcatSubtest subtest) {
        this.subtest = subtest;
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

    public List<GcatOption> getOptions() {
        return options;
    }

    public void setOptions(List<GcatOption> options) {
        this.options = options;
    }
}
