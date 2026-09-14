package com.psychometric.platform.features.assessment.domain.enums;

public enum ExamType {
    PSYCHOMETRIC("التقييم السيكومتري القيادي", "Psychometric Leadership Assessment"),
    EMPLOYMENT("اختبار التوظيف", "Employment Assessment");

    private final String nameAr;
    private final String nameEn;

    ExamType(String nameAr, String nameEn) {
        this.nameAr = nameAr;
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public static ExamType fromString(String value) {
        if (value == null || value.isBlank()) {
            return PSYCHOMETRIC;
        }
        try {
            return ExamType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return PSYCHOMETRIC;
        }
    }
}
