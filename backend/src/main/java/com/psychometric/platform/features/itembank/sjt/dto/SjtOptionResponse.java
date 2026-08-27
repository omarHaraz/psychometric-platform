package com.psychometric.platform.features.itembank.sjt.dto;

import com.psychometric.platform.features.itembank.sjt.entity.SjtOptionKey;

public record SjtOptionResponse(
        Long id,
        SjtOptionKey optionKey,
        String actionTextAr
) {
}
