package com.psychometric.platform.features.itembank.common.controller;

import com.psychometric.platform.features.itembank.common.dto.CloudinaryUploadResponse;
import com.psychometric.platform.features.itembank.common.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/media")
@Tag(name = "Admin Media & CDN API", description = "Endpoints for uploading and managing Cloudinary CDN images")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminMediaController {

    private final CloudinaryService cloudinaryService;

    public AdminMediaController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload image asset to Cloudinary CDN")
    public ResponseEntity<CloudinaryUploadResponse> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false, defaultValue = "itembank/assets") String folder
    ) {
        CloudinaryUploadResponse response = cloudinaryService.uploadImage(file, folder);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping
    @Operation(summary = "Delete image asset from Cloudinary CDN")
    public ResponseEntity<Map<String, String>> deleteMedia(@RequestParam("publicId") String publicId) {
        cloudinaryService.deleteImage(publicId);
        return ResponseEntity.ok(Map.of("message", "Media asset deletion requested successfully"));
    }
}
