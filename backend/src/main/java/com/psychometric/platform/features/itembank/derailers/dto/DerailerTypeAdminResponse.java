package com.psychometric.platform.features.itembank.derailers.dto;

import java.util.List;

public record DerailerTypeAdminResponse(
        Long id,
        String nameAr,
        String definitionAr,
        List<DerailerTypeIndicatorAdminDto> indicators,
        long itemCount
) {
}
