package com.psychometric.platform.features.itembank.common.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.psychometric.platform.common.exception.BadRequestException;
import com.psychometric.platform.features.itembank.common.dto.CloudinaryUploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryService.class);

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/webp", "image/gif", "image/svg+xml"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public CloudinaryUploadResponse uploadImage(MultipartFile file, String folder) {
        validateFile(file);

        String targetFolder = (folder != null && !folder.isBlank()) ? folder.trim() : "psychometric/items";

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", targetFolder,
                            "resource_type", "image"
                    )
            );

            String secureUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            String format = (String) uploadResult.get("format");
            long bytes = uploadResult.get("bytes") instanceof Number n ? n.longValue() : file.getSize();

            log.info("Successfully uploaded image to Cloudinary CDN: publicId={}, url={}", publicId, secureUrl);
            return new CloudinaryUploadResponse(secureUrl, publicId, format != null ? format : "png", bytes);

        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary", e);
            throw new RuntimeException("Failed to upload image to Cloudinary: " + e.getMessage(), e);
        }
    }

    public String uploadPdf(byte[] pdfBytes, String fileName, String folder) {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new BadRequestException("PDF bytes cannot be empty");
        }

        String targetFolder = (folder != null && !folder.isBlank()) ? folder.trim() : "psychometric/reports";
        String publicId = (fileName != null && !fileName.isBlank()) ? fileName.trim() : ("report_" + System.currentTimeMillis());

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    pdfBytes,
                    ObjectUtils.asMap(
                            "folder", targetFolder,
                            "public_id", publicId,
                            "resource_type", "raw"
                    )
            );

            String secureUrl = (String) uploadResult.get("secure_url");
            log.info("Successfully uploaded PDF report to Cloudinary: publicId={}, url={}", publicId, secureUrl);
            return secureUrl;

        } catch (IOException e) {
            log.error("Failed to upload PDF report to Cloudinary", e);
            throw new RuntimeException("Failed to upload PDF report to Cloudinary: " + e.getMessage(), e);
        }
    }

    public void deleteImage(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return;
        }
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Successfully deleted image from Cloudinary: publicId={}", publicId);
        } catch (IOException e) {
            log.error("Failed to delete image from Cloudinary: publicId={}", publicId, e);
            throw new RuntimeException("Failed to delete image from Cloudinary: " + e.getMessage(), e);
        }
    }

    public void deleteImageByUrl(String url) {
        if (url == null || url.isBlank() || !url.contains("cloudinary.com")) {
            return;
        }
        try {
            int uploadIndex = url.indexOf("/upload/");
            if (uploadIndex == -1) return;
            String afterUpload = url.substring(uploadIndex + 8);
            int slashIndex = afterUpload.indexOf('/');
            if (slashIndex == -1) return;
            String pathWithExtension = afterUpload.substring(slashIndex + 1);
            int lastDot = pathWithExtension.lastIndexOf('.');
            String publicId = lastDot != -1 ? pathWithExtension.substring(0, lastDot) : pathWithExtension;
            deleteImage(publicId);
        } catch (Exception e) {
            log.error("Failed to parse Cloudinary URL or delete image: url={}", url, e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Uploaded image file cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("Image file size exceeds maximum limit of 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException("Invalid file format. Allowed formats: JPEG, PNG, WEBP, GIF, SVG");
        }
    }
}
