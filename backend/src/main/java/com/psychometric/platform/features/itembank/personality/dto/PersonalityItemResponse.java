package com.psychometric.platform.features.itembank.personality.dto;

import java.util.List;

public record PersonalityItemResponse(
        Long id,
        String statementAr,
        List<Long> competencyIds
) {
}
