package com.psychometric.platform.features.itembank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.common.exception.BadRequestException;
import com.psychometric.platform.common.exception.GlobalExceptionHandler;
import com.psychometric.platform.common.exception.ResourceNotFoundException;
import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.sjt.controller.AdminSjtItemController;
import com.psychometric.platform.features.itembank.sjt.dto.SjtScenarioAdminRequest;
import com.psychometric.platform.features.itembank.sjt.dto.SjtScenarioAdminResponse;
import com.psychometric.platform.features.itembank.sjt.entity.SjtComplexity;
import com.psychometric.platform.features.itembank.sjt.entity.SjtOptionKey;
import com.psychometric.platform.features.itembank.sjt.service.AdminSjtItemService;
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
class AdminSjtItemControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AdminSjtItemService sjtItemService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        AdminSjtItemController controller = new AdminSjtItemController(sjtItemService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/admin/items/sjt creates new SJT scenario with 201 Created")
    void testCreate() throws Exception {
        SjtScenarioAdminRequest request = new SjtScenarioAdminRequest(
                "SJT-DEC-99", 1L, "عنوان سيناريو تجريبي", "نص السيناريو",
                null, SjtComplexity.DIRECT, SjtOptionKey.B,
                "تعليل", "خطأ", "ملاحظة",
                ExamMode.FULL, List.of()
        );

        SjtScenarioAdminResponse response = new SjtScenarioAdminResponse(
                99L, "SJT-DEC-99", 1L, "اتخاذ القرار",
                "عنوان سيناريو تجريبي", "نص السيناريو", null, SjtComplexity.DIRECT,
                SjtOptionKey.B, "تعليل", "خطأ", "ملاحظة",
                ExamMode.FULL, true, 0, null, List.of()
        );

        when(sjtItemService.create(any(SjtScenarioAdminRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/items/sjt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.bestOptionKey").value("B"))
                .andExpect(jsonPath("$.itemCode").value("SJT-DEC-99"));
    }

    @Test
    @DisplayName("POST /api/admin/items/sjt with invalid domain ID returns 400 Bad Request")
    void testCreate_InvalidDomain() throws Exception {
        SjtScenarioAdminRequest request = new SjtScenarioAdminRequest(
                "SJT-DEC-99", 999L, "عنوان", "نص",
                null, SjtComplexity.DIRECT,
                SjtOptionKey.A, null, null, null,
                ExamMode.FULL, List.of()
        );

        when(sjtItemService.create(any(SjtScenarioAdminRequest.class)))
                .thenThrow(new BadRequestException("SJT Domain not found with ID: 999"));

        mockMvc.perform(post("/api/admin/items/sjt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("SJT Domain not found with ID: 999"));
    }

    @Test
    @DisplayName("GET /api/admin/items/sjt/{id} for non-existing scenario returns 404 Not Found")
    void testGetById_NotFound() throws Exception {
        when(sjtItemService.getById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("SJT Scenario not found with ID: 999"));

        mockMvc.perform(get("/api/admin/items/sjt/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("SJT Scenario not found with ID: 999"));
    }

    @Test
    @DisplayName("PATCH /api/admin/items/sjt/{id}/enable enables scenario with 200 OK")
    void testEnable() throws Exception {
        SjtScenarioAdminResponse response = new SjtScenarioAdminResponse(
                99L, "SJT-DEC-99", 1L, "اتخاذ القرار", "عنوان",
                "نص", null, SjtComplexity.DIRECT, SjtOptionKey.A,
                null, null, null, ExamMode.FULL, true, 0, null, List.of()
        );

        when(sjtItemService.enable(99L)).thenReturn(response);

        mockMvc.perform(patch("/api/admin/items/sjt/99/enable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("PATCH /api/admin/items/sjt/{id}/disable disables scenario with 200 OK")
    void testDisable() throws Exception {
        SjtScenarioAdminResponse response = new SjtScenarioAdminResponse(
                99L, "SJT-DEC-99", 1L, "اتخاذ القرار", "عنوان",
                "نص", null, SjtComplexity.DIRECT, SjtOptionKey.A,
                null, null, null, ExamMode.FULL, false, 0, null, List.of()
        );

        when(sjtItemService.disable(99L)).thenReturn(response);

        mockMvc.perform(patch("/api/admin/items/sjt/99/disable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }
}
