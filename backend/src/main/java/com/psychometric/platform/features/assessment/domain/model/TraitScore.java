package com.psychometric.platform.features.assessment.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "trait_scores")
public class TraitScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_score_id", nullable = false)
    @JsonBackReference("score-traits")
    private AssessmentScore assessmentScore;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trait_id", nullable = false)
    private CompetencyTrait trait;

    @Column(name = "raw_score", nullable = false)
    private Double rawScore = 0.0;

    @Column(name = "score_pct", nullable = false)
    private Double scorePct = 0.0;

    public TraitScore() {
    }

    public TraitScore(AssessmentScore assessmentScore, CompetencyTrait trait, Double rawScore, Double scorePct) {
        this.assessmentScore = assessmentScore;
        this.trait = trait;
        this.rawScore = rawScore;
        this.scorePct = scorePct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AssessmentScore getAssessmentScore() {
        return assessmentScore;
    }

    public void setAssessmentScore(AssessmentScore assessmentScore) {
        this.assessmentScore = assessmentScore;
    }

    public CompetencyTrait getTrait() {
        return trait;
    }

    public void setTrait(CompetencyTrait trait) {
        this.trait = trait;
    }

    public Double getRawScore() {
        return rawScore;
    }

    public void setRawScore(Double rawScore) {
        this.rawScore = rawScore;
    }

    public Double getScorePct() {
        return scorePct;
    }

    public void setScorePct(Double scorePct) {
        this.scorePct = scorePct;
    }
}
