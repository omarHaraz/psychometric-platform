package com.psychometric.platform.features.itembank.derailers.dto;

import com.psychometric.platform.features.itembank.derailers.entity.ResponseScaleType;

public record DerailerItemResponse(
        Long id,
        String statementAr,
        java.util.List<Long> derailerTypeIds,
        ResponseScaleType responseScaleType
) {
}
