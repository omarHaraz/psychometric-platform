package com.psychometric.platform.features.itembank.sjt.dto;

public record SjtDomainResponse(
        Long id,
        String code,
        String nameAr,
        String descriptionAr,
        int displayOrder
) {
}
