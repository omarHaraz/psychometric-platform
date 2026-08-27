package com.psychometric.platform.features.itembank.personality.dto;

public record CompetencyAdminResponse(
        Long id,
        String code,
        String nameAr,
        String definitionAr,
        int displayOrder,
        long itemCount
) {
}
