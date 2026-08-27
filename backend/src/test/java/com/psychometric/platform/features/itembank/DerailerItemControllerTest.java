package com.psychometric.platform.features.itembank;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.derailers.controller.DerailerItemController;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerItemResponse;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerTypeResponse;
import com.psychometric.platform.features.itembank.derailers.entity.ResponseScaleType;
import com.psychometric.platform.features.itembank.derailers.service.DerailerItemService;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DerailerItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DerailerItemService derailerItemService;

    @BeforeEach
    void setUp() {
        DerailerItemController controller = new DerailerItemController(derailerItemService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/items/derailers returns 200 OK with list of DerailerItemResponse")
    void testGetDerailerItems() throws Exception {
        DerailerItemResponse item1 = new DerailerItemResponse(1L, "أفضل التحفظ عند مشاركة المعلومات الحساسة.", 1L, ResponseScaleType.FREQUENCY);
        DerailerItemResponse item2 = new DerailerItemResponse(2L, "أشعر بالقلق المفرط من ارتكاب الأخطاء أمام الفريق.", 2L, ResponseScaleType.LIKERT);

        when(derailerItemService.getDerailerItems(eq(ExamMode.FULL)))
                .thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/api/items/derailers")
                        .param("mode", "FULL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].statementAr").value("أفضل التحفظ عند مشاركة المعلومات الحساسة."))
                .andExpect(jsonPath("$[0].derailerTypeId").value(1))
                .andExpect(jsonPath("$[0].responseScaleType").value("FREQUENCY"))
                .andExpect(jsonPath("$[0].idealTarget").doesNotExist()) // Zero-knowledge perimeter test
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].statementAr").value("أشعر بالقلق المفرط من ارتكاب الأخطاء أمام الفريق."))
                .andExpect(jsonPath("$[1].responseScaleType").value("LIKERT"));
    }

    @Test
    @DisplayName("GET /api/items/derailers/types returns list of DerailerTypeResponse")
    void testGetDerailerTypes() throws Exception {
        DerailerTypeResponse type1 = new DerailerTypeResponse(1L, "المبالغة في الحذر", "تعريف", List.of("التردد"));

        when(derailerItemService.getDerailerTypes()).thenReturn(List.of(type1));

        mockMvc.perform(get("/api/items/derailers/types")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nameAr").value("المبالغة في الحذر"));
    }
}
