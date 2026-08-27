package com.psychometric.platform.features.itembank.gcat.dto;

import com.psychometric.platform.features.itembank.gcat.entity.GcatOptionKey;

public record GcatOptionCandidateDto(
        Long id,
        GcatOptionKey optionKey,
        String optionTextAr,
        String optionImageUrl
) {
}
