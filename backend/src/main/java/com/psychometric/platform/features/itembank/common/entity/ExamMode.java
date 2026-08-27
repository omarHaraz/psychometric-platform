package com.psychometric.platform.features.itembank.common.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ExamMode {
    QUICK,
    FULL,
    BOTH;

    @JsonCreator
    public static ExamMode from(String value) {
        if (value == null || value.isBlank()) return BOTH;
        String normalized = value.trim().toUpperCase();
        for (ExamMode m : values()) {
            if (m.name().equals(normalized)) return m;
        }
        return BOTH;
    }
}
