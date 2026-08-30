package com.psychometric.platform.features.assessment.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import jakarta.persistence.*;

@Entity
@Table(name = "gcat_subtest_scores")
public class GcatSubtestScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_score_id", nullable = false)
    @JsonBackReference("score-gcat")
    private AssessmentScore assessmentScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private GcatSubtestCode subtest;

    @Column(name = "correct_count", nullable = false)
    private Integer correctCount = 0;

    @Column(name = "score_pct", nullable = false)
    private Double scorePct = 0.0;

    public GcatSubtestScore() {
    }

    public GcatSubtestScore(AssessmentScore assessmentScore, GcatSubtestCode subtest, Integer correctCount, Double scorePct) {
        this.assessmentScore = assessmentScore;
        this.subtest = subtest;
        this.correctCount = correctCount;
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

    public GcatSubtestCode getSubtest() {
        return subtest;
    }

    public void setSubtest(GcatSubtestCode subtest) {
        this.subtest = subtest;
    }

    public Integer getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    public Double getScorePct() {
        return scorePct;
    }

    public void setScorePct(Double scorePct) {
        this.scorePct = scorePct;
    }
}
