package com.psychometric.platform.features.itembank.common.dto;

public record CloudinaryUploadResponse(
        String url,
        String publicId,
        String format,
        long bytes
) {
}
