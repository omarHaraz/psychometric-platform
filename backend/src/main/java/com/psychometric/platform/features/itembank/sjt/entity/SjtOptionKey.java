package com.psychometric.platform.features.itembank.sjt.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SjtOptionKey {
    A,
    B,
    C,
    D;

    @JsonCreator
    public static SjtOptionKey from(String value) {
        if (value == null || value.isBlank()) return A;
        String normalized = value.trim().toUpperCase();
        for (SjtOptionKey k : values()) {
            if (k.name().equals(normalized)) return k;
        }
        return A;
    }
}
