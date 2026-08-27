package com.psychometric.platform.features.itembank.sjt.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SjtComplexity {
    DIRECT,
    TRADE_OFF,
    ESCALATION;

    @JsonCreator
    public static SjtComplexity from(String value) {
        if (value == null || value.isBlank()) return DIRECT;
        String normalized = value.trim().toUpperCase().replace("-", "_").replace(" ", "_");
        for (SjtComplexity c : values()) {
            if (c.name().equals(normalized)) return c;
        }
        return DIRECT;
    }
}
