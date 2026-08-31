package com.psychometric.platform.features.assessment.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.psychometric.platform.features.assessment.domain.enums.ReadinessBand;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "assessment_scores")
public class AssessmentScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false, unique = true)
    @JsonIgnore
    private AssessmentAttempt attempt;

    @Column(name = "personality_score_pct", nullable = false)
    private Double personalityScorePct = 0.0;

    @Column(name = "sjt_score_pct", nullable = false)
    private Double sjtScorePct = 0.0;

    @Column(name = "derailers_effective_score_pct", nullable = false)
    private Double derailersEffectiveScorePct = 0.0;

    @Column(name = "cognitive_score_pct", nullable = false)
    private Double cognitiveScorePct = 0.0;

    @Column(name = "composite_score", nullable = false)
    private Double compositeScore = 0.0;

    @Column(name = "percentile", nullable = false)
    private Integer percentile = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "readiness_band", nullable = false, length = 50)
    private ReadinessBand readinessBand;

    @Column(name = "social_desirability_risk_pct")
    private Double socialDesirabilityRiskPct = 0.0;

    @Column(name = "elevated_impression_management")
    private Boolean elevatedImpressionManagement = false;

    @Column(name = "scored_at", nullable = false)
    private Instant scoredAt;

    @OneToMany(mappedBy = "assessmentScore", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("score-traits")
    private Set<TraitScore> traitScores = new HashSet<>();

    @OneToMany(mappedBy = "assessmentScore", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("score-derailers")
    private Set<DerailerCategoryScore> derailerCategoryScores = new HashSet<>();

    @OneToMany(mappedBy = "assessmentScore", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("score-gcat")
    private Set<GcatSubtestScore> gcatSubtestScores = new HashSet<>();

    public AssessmentScore() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AssessmentAttempt getAttempt() {
        return attempt;
    }

    public void setAttempt(AssessmentAttempt attempt) {
        this.attempt = attempt;
    }

    public Double getPersonalityScorePct() {
        return personalityScorePct;
    }

    public void setPersonalityScorePct(Double personalityScorePct) {
        this.personalityScorePct = personalityScorePct;
    }

    public Double getSjtScorePct() {
        return sjtScorePct;
    }

    public void setSjtScorePct(Double sjtScorePct) {
        this.sjtScorePct = sjtScorePct;
    }

    public Double getDerailersEffectiveScorePct() {
        return derailersEffectiveScorePct;
    }

    public void setDerailersEffectiveScorePct(Double derailersEffectiveScorePct) {
        this.derailersEffectiveScorePct = derailersEffectiveScorePct;
    }

    public Double getCognitiveScorePct() {
        return cognitiveScorePct;
    }

    public void setCognitiveScorePct(Double cognitiveScorePct) {
        this.cognitiveScorePct = cognitiveScorePct;
    }

    public Double getCompositeScore() {
        return compositeScore;
    }

    public void setCompositeScore(Double compositeScore) {
        this.compositeScore = compositeScore;
    }

    public Integer getPercentile() {
        return percentile;
    }

    public void setPercentile(Integer percentile) {
        this.percentile = percentile;
    }

    public ReadinessBand getReadinessBand() {
        return readinessBand;
    }

    public void setReadinessBand(ReadinessBand readinessBand) {
        this.readinessBand = readinessBand;
    }

    public Instant getScoredAt() {
        return scoredAt;
    }

    public void setScoredAt(Instant scoredAt) {
        this.scoredAt = scoredAt;
    }

    public Set<TraitScore> getTraitScores() {
        return traitScores;
    }

    public void setTraitScores(Set<TraitScore> traitScores) {
        this.traitScores = traitScores;
    }

    public Set<DerailerCategoryScore> getDerailerCategoryScores() {
        return derailerCategoryScores;
    }

    public void setDerailerCategoryScores(Set<DerailerCategoryScore> derailerCategoryScores) {
        this.derailerCategoryScores = derailerCategoryScores;
    }

    public Set<GcatSubtestScore> getGcatSubtestScores() {
        return gcatSubtestScores;
    }

    public void setGcatSubtestScores(Set<GcatSubtestScore> gcatSubtestScores) {
        this.gcatSubtestScores = gcatSubtestScores;
    }

    public Double getSocialDesirabilityRiskPct() {
        return socialDesirabilityRiskPct;
    }

    public void setSocialDesirabilityRiskPct(Double socialDesirabilityRiskPct) {
        this.socialDesirabilityRiskPct = socialDesirabilityRiskPct;
    }

    public Boolean getElevatedImpressionManagement() {
        return elevatedImpressionManagement;
    }

    public void setElevatedImpressionManagement(Boolean elevatedImpressionManagement) {
        this.elevatedImpressionManagement = elevatedImpressionManagement;
    }
}
