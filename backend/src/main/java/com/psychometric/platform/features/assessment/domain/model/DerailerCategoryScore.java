package com.psychometric.platform.features.assessment.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "derailer_category_scores")
public class DerailerCategoryScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_score_id", nullable = false)
    @JsonBackReference("score-derailers")
    private AssessmentScore assessmentScore;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private DerailerCategory category;

    @Column(name = "raw_score", nullable = false)
    private Double rawScore = 0.0;

    @Column(name = "score_pct", nullable = false)
    private Double scorePct = 0.0;

    public DerailerCategoryScore() {
    }

    public DerailerCategoryScore(AssessmentScore assessmentScore, DerailerCategory category, Double rawScore, Double scorePct) {
        this.assessmentScore = assessmentScore;
        this.category = category;
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

    public DerailerCategory getCategory() {
        return category;
    }

    public void setCategory(DerailerCategory category) {
        this.category = category;
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
