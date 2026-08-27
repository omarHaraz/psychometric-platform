package com.psychometric.platform.features.itembank.sjt.entity;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sjt_scenarios")
public class SjtScenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_code", nullable = false, unique = true, length = 100)
    private String itemCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private SjtDomain domain;

    @Column(name = "title_ar", nullable = false, length = 255)
    private String titleAr;

    @Column(name = "narrative_ar", nullable = false, columnDefinition = "TEXT")
    private String narrativeAr;

    @Column(name = "scenario_image_url", length = 500)
    private String scenarioImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "complexity", nullable = false, length = 30, columnDefinition = "VARCHAR(30)")
    private SjtComplexity complexity = SjtComplexity.DIRECT;

    @Enumerated(EnumType.STRING)
    @Column(name = "best_option_key", nullable = false, length = 10)
    private SjtOptionKey bestOptionKey;

    @Column(name = "rationale_ar", columnDefinition = "TEXT")
    private String rationaleAr;

    @Column(name = "common_mistake_ar", columnDefinition = "TEXT")
    private String commonMistakeAr;

    @Column(name = "coaching_note_ar", columnDefinition = "TEXT")
    private String coachingNoteAr;

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

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SjtOption> options = new ArrayList<>();

    public SjtScenario() {
    }

    public SjtScenario(String itemCode, SjtDomain domain, String titleAr, String narrativeAr,
                       String scenarioImageUrl, SjtComplexity complexity, SjtOptionKey bestOptionKey,
                       String rationaleAr, String commonMistakeAr, String coachingNoteAr,
                       ExamMode examMode) {
        this.itemCode = itemCode;
        this.domain = domain;
        this.titleAr = titleAr;
        this.narrativeAr = narrativeAr;
        this.scenarioImageUrl = scenarioImageUrl;
        this.complexity = complexity;
        this.bestOptionKey = bestOptionKey;
        this.rationaleAr = rationaleAr;
        this.commonMistakeAr = commonMistakeAr;
        this.coachingNoteAr = coachingNoteAr;
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

    public SjtDomain getDomain() {
        return domain;
    }

    public void setDomain(SjtDomain domain) {
        this.domain = domain;
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

    public List<SjtOption> getOptions() {
        return options;
    }

    public void setOptions(List<SjtOption> options) {
        this.options = options;
    }
}
