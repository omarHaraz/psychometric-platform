package com.psychometric.platform.features.itembank;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.psychometric.platform.common.exception.BadRequestException;
import com.psychometric.platform.features.itembank.common.dto.CloudinaryUploadResponse;
import com.psychometric.platform.features.itembank.common.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloudinaryServiceTest {

    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        cloudinary = spy(new Cloudinary(Map.of(
                "cloud_name", "n8ebougz",
                "api_key", "811295576953758",
                "api_secret", "UCkrZ7QOJntAUNflVQbxnOwo4Fo"
        )));
        cloudinaryService = new CloudinaryService(cloudinary);
    }

    @Test
    @DisplayName("uploadImage successfully uploads binary image to Cloudinary and returns secure URL")
    void testUploadImage_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "shape.png", "image/png", "test-bytes".getBytes()
        );

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(Map.of(
                "secure_url", "https://res.cloudinary.com/n8ebougz/image/upload/v1/gcat/shape.png",
                "public_id", "gcat/shape",
                "format", "png",
                "bytes", 1024L
        ));

        CloudinaryUploadResponse response = cloudinaryService.uploadImage(file, "gcat");

        assertThat(response).isNotNull();
        assertThat(response.url()).isEqualTo("https://res.cloudinary.com/n8ebougz/image/upload/v1/gcat/shape.png");
        assertThat(response.publicId()).isEqualTo("gcat/shape");
        assertThat(response.format()).isEqualTo("png");
    }

    @Test
    @DisplayName("uploadImage rejects non-image files with BadRequestException")
    void testUploadImage_NonImageValidation() {
        MockMultipartFile textFile = new MockMultipartFile(
                "file", "data.txt", "text/plain", "plain text".getBytes()
        );

        assertThatThrownBy(() -> cloudinaryService.uploadImage(textFile, "assets"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid file format");
    }

    @Test
    @DisplayName("uploadImage rejects empty files with BadRequestException")
    void testUploadImage_EmptyFileValidation() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.png", "image/png", new byte[0]
        );

        assertThatThrownBy(() -> cloudinaryService.uploadImage(emptyFile, "assets"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Uploaded image file cannot be empty");
    }

    @Test
    @DisplayName("deleteImage calls Cloudinary destroy API for valid publicId")
    void testDeleteImage_Success() throws IOException {
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(eq("gcat/shape"), anyMap())).thenReturn(Map.of("result", "ok"));

        cloudinaryService.deleteImage("gcat/shape");

        verify(uploader).destroy(eq("gcat/shape"), anyMap());
    }
}
