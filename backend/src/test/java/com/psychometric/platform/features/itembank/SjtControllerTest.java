package com.psychometric.platform.features.itembank;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.sjt.controller.SjtController;
import com.psychometric.platform.features.itembank.sjt.dto.SjtDomainResponse;
import com.psychometric.platform.features.itembank.sjt.dto.SjtOptionResponse;
import com.psychometric.platform.features.itembank.sjt.dto.SjtScenarioResponse;
import com.psychometric.platform.features.itembank.sjt.entity.SjtComplexity;
import com.psychometric.platform.features.itembank.sjt.entity.SjtOptionKey;
import com.psychometric.platform.features.itembank.sjt.service.SjtService;
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
class SjtControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SjtService sjtService;

    @BeforeEach
    void setUp() {
        SjtController controller = new SjtController(sjtService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/items/sjt/scenarios returns sanitized scenario list without scoring rubrics")
    void testGetScenarios() throws Exception {
        SjtOptionResponse optA = new SjtOptionResponse(1L, SjtOptionKey.A, "خيار أ");
        SjtOptionResponse optB = new SjtOptionResponse(2L, SjtOptionKey.B, "خيار ب");

        SjtScenarioResponse scenario = new SjtScenarioResponse(
                10L, "SJT-DEC-01", 1L,
                "عنوان السيناريو", "نص السيناريو", null, SjtComplexity.TRADE_OFF,
                List.of(optA, optB)
        );

        when(sjtService.getCandidateScenarios(eq(null), eq(ExamMode.FULL)))
                .thenReturn(List.of(scenario));

        mockMvc.perform(get("/api/items/sjt/scenarios")
                        .param("mode", "FULL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].itemCode").value("SJT-DEC-01"))
                .andExpect(jsonPath("$[0].domainId").value(1))
                .andExpect(jsonPath("$[0].titleAr").value("عنوان السيناريو"))
                .andExpect(jsonPath("$[0].options.length()").value(2))
                .andExpect(jsonPath("$[0].bestOptionKey").doesNotExist())
                .andExpect(jsonPath("$[0].rationaleAr").doesNotExist())
                .andExpect(jsonPath("$[0].commonMistakeAr").doesNotExist())
                .andExpect(jsonPath("$[0].coachingNoteAr").doesNotExist());
    }

    @Test
    @DisplayName("GET /api/items/sjt/domains returns 5 SJT domains taxonomy")
    void testGetDomains() throws Exception {
        SjtDomainResponse domain = new SjtDomainResponse(1L, "DECISION_INTEGRITY", "اتخاذ القرار", "الوصف", 1);

        when(sjtService.getAllDomains()).thenReturn(List.of(domain));

        mockMvc.perform(get("/api/items/sjt/domains")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("DECISION_INTEGRITY"))
                .andExpect(jsonPath("$[0].nameAr").value("اتخاذ القرار"));
    }

    @Test
    @DisplayName("GET /api/items/sjt/scenarios/{id} returns scenario by id")
    void testGetScenarioById() throws Exception {
        SjtScenarioResponse scenario = new SjtScenarioResponse(
                20L, "SJT-COM-01", 1L,
                "عنوان", "نص", null, SjtComplexity.DIRECT,
                List.of()
        );

        when(sjtService.getScenarioById(eq(20L)))
                .thenReturn(scenario);

        mockMvc.perform(get("/api/items/sjt/scenarios/20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.itemCode").value("SJT-COM-01"));
    }
}
