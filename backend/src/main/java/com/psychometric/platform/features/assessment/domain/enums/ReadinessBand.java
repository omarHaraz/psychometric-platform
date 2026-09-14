package com.psychometric.platform.features.assessment.domain.enums;

public enum ReadinessBand {
    EXCELLENT("Excellent", "ممتاز", 90.0, 100.0),
    STRONG("Strong", "قوي", 80.0, 89.999),
    ACCEPTABLE("Acceptable", "مقبول", 70.0, 79.999),
    FOUNDATIONAL_ADVANCED("Foundational Advanced", "تأسيسي متقدم", 50.0, 69.999),
    FOUNDATIONAL("Foundational", "تأسيسي", 0.0, 49.999);

    private final String labelEn;
    private final String labelAr;
    private final double minScore;
    private final double maxScore;

    ReadinessBand(String labelEn, String labelAr, double minScore, double maxScore) {
        this.labelEn = labelEn;
        this.labelAr = labelAr;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    public String getLabelEn() {
        return labelEn;
    }

    public String getLabelAr() {
        return labelAr;
    }

    public double getMinScore() {
        return minScore;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public String getLabelAr(String examType) {
        if ("EMPLOYMENT".equalsIgnoreCase(examType)) {
            return switch (this) {
                case EXCELLENT -> "ممتاز";
                case STRONG -> "جيد جداً";
                case ACCEPTABLE -> "جيد";
                case FOUNDATIONAL_ADVANCED, FOUNDATIONAL -> "ضعيف";
            };
        }
        return this.labelAr;
    }

    public static ReadinessBand fromCompositeScore(double score) {
        return fromCompositeScore(score, "PSYCHOMETRIC");
    }

    public static ReadinessBand fromCompositeScore(double score, String examType) {
        if ("EMPLOYMENT".equalsIgnoreCase(examType)) {
            if (score >= 85.0) return EXCELLENT;
            if (score >= 75.0) return STRONG;
            if (score >= 60.0) return ACCEPTABLE;
            return FOUNDATIONAL;
        }
        if (score >= 90.0) return EXCELLENT;
        if (score >= 80.0) return STRONG;
        if (score >= 70.0) return ACCEPTABLE;
        if (score >= 50.0) return FOUNDATIONAL_ADVANCED;
        return FOUNDATIONAL;
    }
}
