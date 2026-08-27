package com.psychometric.platform.features.itembank.common.entity;

public enum FrequencyScale {
    NEVER(1, "أبداً"),
    RARELY(2, "نادراً"),
    SOMETIMES(3, "أحياناً"),
    OFTEN(4, "غالباً"),
    ALWAYS(5, "دائماً");

    private final int score;
    private final String labelAr;

    FrequencyScale(int score, String labelAr) {
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
