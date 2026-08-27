package com.psychometric.platform.features.itembank.sjt.dto;

import com.psychometric.platform.features.itembank.sjt.entity.SjtComplexity;

import java.util.List;

public record SjtScenarioResponse(
        Long id,
        String itemCode,
        Long domainId,
        String titleAr,
        String narrativeAr,
        String scenarioImageUrl,
        SjtComplexity complexity,
        List<SjtOptionResponse> options
) {
}
