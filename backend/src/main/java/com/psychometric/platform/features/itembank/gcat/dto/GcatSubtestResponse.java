package com.psychometric.platform.features.itembank.gcat.dto;

import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;

public record GcatSubtestResponse(
        Long id,
        GcatSubtestCode code,
        String nameAr,
        String descriptionAr,
        int fullModeQuota,
        int quickModeQuota,
        Integer timeLimitSeconds
) {
}
