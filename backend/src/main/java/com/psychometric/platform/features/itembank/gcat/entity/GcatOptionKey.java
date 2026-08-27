package com.psychometric.platform.features.itembank.gcat.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GcatOptionKey {
    A,
    B,
    C,
    D,
    E;

    @JsonCreator
    public static GcatOptionKey from(String value) {
        if (value == null || value.isBlank()) return A;
        String normalized = value.trim().toUpperCase();
        for (GcatOptionKey k : values()) {
            if (k.name().equals(normalized)) return k;
        }
        return A;
    }
}
