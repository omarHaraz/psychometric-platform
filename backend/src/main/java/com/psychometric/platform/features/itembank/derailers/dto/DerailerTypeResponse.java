package com.psychometric.platform.features.itembank.derailers.dto;

import java.util.List;

public record DerailerTypeResponse(
        Long id,
        String nameAr,
        String definitionAr,
        List<String> indicators
) {
}
