package com.psychometric.platform.features.itembank;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.personality.controller.PersonalityItemController;
import com.psychometric.platform.features.itembank.personality.dto.PersonalityItemResponse;
import com.psychometric.platform.features.itembank.personality.service.PersonalityItemService;
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
class PersonalityItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonalityItemService personalityItemService;

    @BeforeEach
    void setUp() {
        PersonalityItemController controller = new PersonalityItemController(personalityItemService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/items/personality returns 200 OK with list of PersonalityItemResponse")
    void testGetPersonalityItems() throws Exception {
        PersonalityItemResponse item1 = new PersonalityItemResponse(1L, "أفضل التخطيط المسبق دائماً.", 101L);
        PersonalityItemResponse item2 = new PersonalityItemResponse(2L, "أتكيف بسهولة مع التغييرات المفاجئة.", 102L);

        when(personalityItemService.getPersonalityItems(eq(ExamMode.QUICK)))
                .thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/api/items/personality")
                        .param("mode", "QUICK")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].statementAr").value("أفضل التخطيط المسبق دائماً."))
                .andExpect(jsonPath("$[0].competencyId").value(101))
                .andExpect(jsonPath("$[0].idealTarget").doesNotExist()) // Zero-knowledge: candidate DTO must NOT leak idealTarget
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].statementAr").value("أتكيف بسهولة مع التغييرات المفاجئة."))
                .andExpect(jsonPath("$[1].competencyId").value(102));
    }

    @Test
    @DisplayName("GET /api/items/personality without mode param defaults to QUICK mode")
    void testGetPersonalityItems_DefaultMode() throws Exception {
        when(personalityItemService.getPersonalityItems(eq(ExamMode.QUICK)))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/items/personality")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }
}
