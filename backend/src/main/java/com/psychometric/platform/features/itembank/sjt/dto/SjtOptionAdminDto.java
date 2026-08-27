package com.psychometric.platform.features.itembank.sjt.dto;

import com.psychometric.platform.features.itembank.sjt.entity.SjtOptionKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SjtOptionAdminDto {
    private Long id;

    @NotNull(message = "مفتاح الخيار مطلوب")
    private SjtOptionKey optionKey;

    @NotBlank(message = "نص الإجراء مطلوب")
    private String actionTextAr;

    @NotNull(message = "درجة الفاعلية مطلوبة")
    private Double effectivenessScore = 0.0;

    private String scoringRationaleAr;
    private boolean bestAction = false;

    public SjtOptionAdminDto() {
    }

    public SjtOptionAdminDto(Long id, SjtOptionKey optionKey, String actionTextAr, Double effectivenessScore, String scoringRationaleAr, boolean bestAction) {
        this.id = id;
        this.optionKey = optionKey;
        this.actionTextAr = actionTextAr;
        this.effectivenessScore = effectivenessScore;
        this.scoringRationaleAr = scoringRationaleAr;
        this.bestAction = bestAction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isBestAction() {
        return bestAction;
    }

    public void setBestAction(boolean bestAction) {
        this.bestAction = bestAction;
    }
}
