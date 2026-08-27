package com.psychometric.platform.features.itembank.gcat.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GcatSubtestCode {
    ABSTRACT,
    NUMERICAL,
    VERBAL;

    @JsonCreator
    public static GcatSubtestCode from(String value) {
        if (value == null || value.isBlank()) return ABSTRACT;
        String normalized = value.trim().toUpperCase();
        for (GcatSubtestCode c : values()) {
            if (c.name().equals(normalized)) return c;
        }
        return ABSTRACT;
    }
}
