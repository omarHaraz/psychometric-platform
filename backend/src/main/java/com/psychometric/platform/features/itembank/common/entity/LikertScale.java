package com.psychometric.platform.features.itembank.common.entity;

public enum LikertScale {
    STRONGLY_DISAGREE(1, "لا أتفق بشدة"),
    DISAGREE(2, "لا أتفق"),
    NEUTRAL(3, "محايد"),
    AGREE(4, "أتفق"),
    STRONGLY_AGREE(5, "أتفق بشدة");

    private final int score;
    private final String labelAr;

    LikertScale(int score, String labelAr) {
        this.score = score;
        this.labelAr = labelAr;
    }

    public int getScore() {
        return score;
    }

    public String getLabelAr() {
        return labelAr;
    }
}
