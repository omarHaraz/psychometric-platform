package com.psychometric.platform.features.itembank.gcat.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GcatDifficulty {
    EASY,
    MEDIUM,
    HARD;

    @JsonCreator
    public static GcatDifficulty from(String value) {
        if (value == null || value.isBlank()) return MEDIUM;
        String normalized = value.trim().toUpperCase();
        for (GcatDifficulty d : values()) {
            if (d.name().equals(normalized)) return d;
        }
        return MEDIUM;
    }
}
