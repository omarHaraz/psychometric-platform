package com.psychometric.platform.features.itembank.sjt.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sjt_options")
public class SjtOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id", nullable = false)
    private SjtScenario scenario;

    @Enumerated(EnumType.STRING)
    @Column(name = "option_key", nullable = false, length = 10)
    private SjtOptionKey optionKey;

    @Column(name = "action_text_ar", nullable = false, columnDefinition = "TEXT")
    private String actionTextAr;

    @Column(name = "effectiveness_score", nullable = false)
    private Double effectivenessScore = 0.0;

    @Column(name = "scoring_rationale_ar", columnDefinition = "TEXT")
    private String scoringRationaleAr;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 1;

    @Column(name = "is_best_action", nullable = false)
    private boolean bestAction = false;

    public SjtOption() {
    }

    public SjtOption(SjtScenario scenario, SjtOptionKey optionKey, String actionTextAr, Double effectivenessScore, String scoringRationaleAr, boolean bestAction) {
        this.scenario = scenario;
        this.optionKey = optionKey;
        this.actionTextAr = actionTextAr;
        this.effectivenessScore = effectivenessScore;
        this.scoringRationaleAr = scoringRationaleAr;
        this.bestAction = bestAction;
        this.displayOrder = optionKey != null ? optionKey.ordinal() + 1 : 1;
    }

    public SjtOption(SjtScenario scenario, SjtOptionKey optionKey, String actionTextAr, Double effectivenessScore, String scoringRationaleAr, boolean bestAction, Integer displayOrder) {
        this.scenario = scenario;
        this.optionKey = optionKey;
        this.actionTextAr = actionTextAr;
        this.effectivenessScore = effectivenessScore;
        this.scoringRationaleAr = scoringRationaleAr;
        this.bestAction = bestAction;
        this.displayOrder = displayOrder != null ? displayOrder : (optionKey != null ? optionKey.ordinal() + 1 : 1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SjtScenario getScenario() {
        return scenario;
    }

    public void setScenario(SjtScenario scenario) {
        this.scenario = scenario;
    }

    public SjtOptionKey getOptionKey() {
        return optionKey;
    }

    public void setOptionKey(SjtOptionKey optionKey) {
        this.optionKey = optionKey;
    }

    public String getActionTextAr() {
        return actionTextAr;
    }

    public void setActionTextAr(String actionTextAr) {
        this.actionTextAr = actionTextAr;
    }

    public Double getEffectivenessScore() {
        return effectivenessScore;
    }

    public void setEffectivenessScore(Double effectivenessScore) {
        this.effectivenessScore = effectivenessScore;
    }

    public String getScoringRationaleAr() {
        return scoringRationaleAr;
    }

    public void setScoringRationaleAr(String scoringRationaleAr) {
        this.scoringRationaleAr = scoringRationaleAr;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isBestAction() {
        return bestAction;
    }

    public void setBestAction(boolean bestAction) {
        this.bestAction = bestAction;
    }
}
