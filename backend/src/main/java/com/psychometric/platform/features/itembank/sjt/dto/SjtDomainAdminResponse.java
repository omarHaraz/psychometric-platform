package com.psychometric.platform.features.itembank.sjt.dto;

public record SjtDomainAdminResponse(
        Long id,
        String code,
        String nameAr,
        String descriptionAr,
        int displayOrder,
        long scenarioCount
) {
}
