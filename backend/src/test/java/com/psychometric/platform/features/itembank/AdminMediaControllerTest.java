package com.psychometric.platform.features.itembank;

import com.psychometric.platform.common.exception.GlobalExceptionHandler;
import com.psychometric.platform.features.itembank.common.controller.AdminMediaController;
import com.psychometric.platform.features.itembank.common.dto.CloudinaryUploadResponse;
import com.psychometric.platform.features.itembank.common.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminMediaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        AdminMediaController controller = new AdminMediaController(cloudinaryService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/admin/media/upload successfully uploads image and returns CDN details")
    void testUploadMedia_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "matrix_pattern.png", "image/png", "dummy-image-content".getBytes()
        );

        CloudinaryUploadResponse response = new CloudinaryUploadResponse(
                "https://res.cloudinary.com/psychometric/image/upload/v1/gcat/matrices/sample.png",
                "gcat/matrices/sample",
                "png",
                1024L
        );

        when(cloudinaryService.uploadImage(any(), eq("gcat/matrices"))).thenReturn(response);

        mockMvc.perform(multipart("/api/admin/media/upload")
                        .file(file)
                        .param("folder", "gcat/matrices"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url").value(response.url()))
                .andExpect(jsonPath("$.publicId").value(response.publicId()))
                .andExpect(jsonPath("$.format").value("png"));
    }

    @Test
    @DisplayName("DELETE /api/admin/media successfully deletes image from CDN")
    void testDeleteMedia_Success() throws Exception {
        doNothing().when(cloudinaryService).deleteImage(eq("gcat/matrices/sample"));

        mockMvc.perform(delete("/api/admin/media")
                        .param("publicId", "gcat/matrices/sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Media asset deletion requested successfully"));

        verify(cloudinaryService).deleteImage("gcat/matrices/sample");
    }
}
