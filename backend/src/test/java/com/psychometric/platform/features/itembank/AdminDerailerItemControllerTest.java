package com.psychometric.platform.features.itembank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.common.exception.BadRequestException;
import com.psychometric.platform.common.exception.GlobalExceptionHandler;
import com.psychometric.platform.common.exception.ResourceNotFoundException;
import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.derailers.controller.AdminDerailerItemController;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerItemAdminRequest;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerItemAdminResponse;
import com.psychometric.platform.features.itembank.derailers.entity.ResponseScaleType;
import com.psychometric.platform.features.itembank.derailers.service.AdminDerailerItemService;
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
class AdminDerailerItemControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AdminDerailerItemService derailerItemService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        AdminDerailerItemController controller = new AdminDerailerItemController(derailerItemService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/admin/items/derailers creates new derailer item with 201 Created")
    void testCreate() throws Exception {
        DerailerItemAdminRequest request = new DerailerItemAdminRequest(
                "أشعر بالتردد عند اتخاذ قرارات حاسمة تحت الضغط.",
                1L, 1, ResponseScaleType.FREQUENCY, ExamMode.FULL
        );

        DerailerItemAdminResponse response = new DerailerItemAdminResponse(
                5L, request.getStatementAr(), 1L, "المبالغة في الحذر والتوجس", 1,
                ResponseScaleType.FREQUENCY, ExamMode.FULL, true, 0, null
        );

        when(derailerItemService.create(any(DerailerItemAdminRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/items/derailers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.idealTarget").value(1))
                .andExpect(jsonPath("$.statementAr").value(request.getStatementAr()));
    }

    @Test
    @DisplayName("POST /api/admin/items/derailers with invalid type FK returns 400 Bad Request")
    void testCreate_InvalidTypeFk() throws Exception {
        DerailerItemAdminRequest request = new DerailerItemAdminRequest(
                "بيان تجريبي", 999L, 1, ResponseScaleType.LIKERT, ExamMode.FULL
        );

        when(derailerItemService.create(any(DerailerItemAdminRequest.class)))
                .thenThrow(new BadRequestException("DerailerType not found with ID: 999"));

        mockMvc.perform(post("/api/admin/items/derailers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("DerailerType not found with ID: 999"));
    }

    @Test
    @DisplayName("GET /api/admin/items/derailers lists all derailer items")
    void testGetAll() throws Exception {
        DerailerItemAdminResponse item = new DerailerItemAdminResponse(
                1L, "عبارة", 1L, "التحفظ", 1, ResponseScaleType.FREQUENCY, ExamMode.FULL, true, 2, null
        );

        when(derailerItemService.getAll()).thenReturn(List.of(item));

        mockMvc.perform(get("/api/admin/items/derailers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].idealTarget").value(1));
    }

    @Test
    @DisplayName("GET /api/admin/items/derailers/{id} for non-existing item returns 404 Not Found")
    void testGetById_NotFound() throws Exception {
        when(derailerItemService.getById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("DerailerItem not found with ID: 999"));

        mockMvc.perform(get("/api/admin/items/derailers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("DerailerItem not found with ID: 999"));
    }

    @Test
    @DisplayName("DELETE /api/admin/items/derailers/{id} soft-deletes item with 204 No Content")
    void testSoftDelete() throws Exception {
        doNothing().when(derailerItemService).softDelete(5L);

        mockMvc.perform(delete("/api/admin/items/derailers/5"))
                .andExpect(status().isNoContent());

        verify(derailerItemService).softDelete(5L);
    }

    @Test
    @DisplayName("PATCH /api/admin/items/derailers/{id}/enable enables item with 200 OK")
    void testEnable() throws Exception {
        DerailerItemAdminResponse response = new DerailerItemAdminResponse(
                5L, "نص", 1L, "التحفظ", 1, ResponseScaleType.FREQUENCY, ExamMode.FULL, true, 0, null
        );

        when(derailerItemService.enable(5L)).thenReturn(response);

        mockMvc.perform(patch("/api/admin/items/derailers/5/enable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("PATCH /api/admin/items/derailers/{id}/disable disables item with 200 OK")
    void testDisable() throws Exception {
        DerailerItemAdminResponse response = new DerailerItemAdminResponse(
                5L, "نص", 1L, "التحفظ", 1, ResponseScaleType.FREQUENCY, ExamMode.FULL, false, 0, null
        );

        when(derailerItemService.disable(5L)).thenReturn(response);

        mockMvc.perform(patch("/api/admin/items/derailers/5/disable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }
}
