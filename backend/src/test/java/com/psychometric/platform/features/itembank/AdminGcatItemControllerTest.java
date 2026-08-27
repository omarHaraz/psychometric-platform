package com.psychometric.platform.features.itembank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.common.exception.BadRequestException;
import com.psychometric.platform.common.exception.GlobalExceptionHandler;
import com.psychometric.platform.common.exception.ResourceNotFoundException;
import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.gcat.controller.AdminGcatItemController;
import com.psychometric.platform.features.itembank.gcat.dto.GcatQuestionAdminRequest;
import com.psychometric.platform.features.itembank.gcat.dto.GcatQuestionAdminResponse;
import com.psychometric.platform.features.itembank.gcat.entity.GcatDifficulty;
import com.psychometric.platform.features.itembank.gcat.entity.GcatOptionKey;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import com.psychometric.platform.features.itembank.gcat.service.AdminGcatItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminGcatItemControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AdminGcatItemService gcatItemService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        AdminGcatItemController controller = new AdminGcatItemController(gcatItemService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/admin/items/cognitive creates new question with 201 Created")
    void testCreate() throws Exception {
        GcatQuestionAdminRequest request = new GcatQuestionAdminRequest(
                "GCAT-ABS-99", GcatSubtestCode.ABSTRACT, "عنوان تجريبي",
                "نص التوجيه", null, null, "الدوران",
                "ملاحظة", "قاعدة", "تطبيق",
                GcatOptionKey.B, GcatDifficulty.EASY, ExamMode.FULL, List.of()
        );

        GcatQuestionAdminResponse response = new GcatQuestionAdminResponse(
                99L, "GCAT-ABS-99", GcatSubtestCode.ABSTRACT,
                "عنوان تجريبي", "نص التوجيه", null, null,
                "الدوران", "ملاحظة", "قاعدة", "تطبيق",
                GcatOptionKey.B, GcatDifficulty.EASY, ExamMode.FULL, true, 0, null, List.of()
        );

        when(gcatItemService.create(any(GcatQuestionAdminRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/items/cognitive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.correctOptionKey").value("B"))
                .andExpect(jsonPath("$.itemCode").value("GCAT-ABS-99"));
    }

    @Test
    @DisplayName("POST /api/admin/items/cognitive with duplicate itemCode returns 400 Bad Request")
    void testCreate_DuplicateItemCode() throws Exception {
        GcatQuestionAdminRequest request = new GcatQuestionAdminRequest(
                "GCAT-ABS-01", GcatSubtestCode.ABSTRACT, "عنوان",
                null, null, null, null,
                null, null, null,
                GcatOptionKey.A, GcatDifficulty.EASY, ExamMode.FULL, List.of()
        );

        when(gcatItemService.create(any(GcatQuestionAdminRequest.class)))
                .thenThrow(new BadRequestException("GCAT Question already exists with itemCode: GCAT-ABS-01"));

        mockMvc.perform(post("/api/admin/items/cognitive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("GCAT Question already exists with itemCode: GCAT-ABS-01"));
    }

    @Test
    @DisplayName("GET /api/admin/items/cognitive/{id} for non-existing question returns 404 Not Found")
    void testGetById_NotFound() throws Exception {
        when(gcatItemService.getById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("GCAT Question not found with ID: 999"));

        mockMvc.perform(get("/api/admin/items/cognitive/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("GCAT Question not found with ID: 999"));
    }

    @Test
    @DisplayName("PATCH /api/admin/items/cognitive/{id}/enable enables question with 200 OK")
    void testEnable() throws Exception {
        GcatQuestionAdminResponse response = new GcatQuestionAdminResponse(
                99L, "GCAT-ABS-99", GcatSubtestCode.ABSTRACT, "عنوان",
                null, null, null, null, null, null, null,
                GcatOptionKey.A, GcatDifficulty.EASY, ExamMode.FULL, true, 0, null, List.of()
        );

        when(gcatItemService.enable(99L)).thenReturn(response);

        mockMvc.perform(patch("/api/admin/items/cognitive/99/enable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("PATCH /api/admin/items/cognitive/{id}/disable disables question with 200 OK")
    void testDisable() throws Exception {
        GcatQuestionAdminResponse response = new GcatQuestionAdminResponse(
                99L, "GCAT-ABS-99", GcatSubtestCode.ABSTRACT, "عنوان",
                null, null, null, null, null, null, null,
                GcatOptionKey.A, GcatDifficulty.EASY, ExamMode.FULL, false, 0, null, List.of()
        );

        when(gcatItemService.disable(99L)).thenReturn(response);

        mockMvc.perform(patch("/api/admin/items/cognitive/99/disable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }
}
