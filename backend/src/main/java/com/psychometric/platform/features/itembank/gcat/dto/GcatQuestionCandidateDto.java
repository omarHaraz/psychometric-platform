package com.psychometric.platform.features.itembank.gcat.dto;

import com.psychometric.platform.features.itembank.gcat.entity.GcatDifficulty;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;

import java.util.List;

public record GcatQuestionCandidateDto(
        Long id,
        String itemCode,
        GcatSubtestCode subtestCode,
        String titleAr,
        String promptTextAr,
        String questionImageUrl,
        GcatDifficulty difficulty,
        List<GcatOptionCandidateDto> options
) {
}
