package com.psychometric.platform.features.itembank;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerItemResponse;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerTypeResponse;
import com.psychometric.platform.features.itembank.derailers.entity.DerailerItem;
import com.psychometric.platform.features.itembank.derailers.entity.DerailerType;
import com.psychometric.platform.features.itembank.derailers.entity.DerailerTypeIndicator;
import com.psychometric.platform.features.itembank.derailers.entity.ResponseScaleType;
import com.psychometric.platform.features.itembank.derailers.repository.DerailerItemRepository;
import com.psychometric.platform.features.itembank.derailers.repository.DerailerTypeRepository;
import com.psychometric.platform.features.itembank.derailers.service.DerailerItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DerailerItemServiceTest {

    @Mock
    private DerailerItemRepository derailerItemRepository;

    @Mock
    private DerailerTypeRepository derailerTypeRepository;

    private DerailerItemService derailerItemService;

    private DerailerType testType;
    private DerailerItem itemFull;
    private DerailerItem itemBoth;

    @BeforeEach
    void setUp() {
        derailerItemService = new DerailerItemService(derailerItemRepository, derailerTypeRepository);

        testType = new DerailerType("التحفظ", "تعريف");
        testType.setId(1L);

        itemFull = new DerailerItem("عبارة 1", testType, 1, ResponseScaleType.FREQUENCY, ExamMode.FULL);
        itemFull.setId(10L);

        itemBoth = new DerailerItem("عبارة 2", testType, 5, ResponseScaleType.FREQUENCY, ExamMode.BOTH);
        itemBoth.setId(20L);
    }

    @Test
    @DisplayName("FULL mode queries items with ExamMode FULL and BOTH")
    void testGetDerailerItems_FullMode() {
        when(derailerItemRepository.findByExamModeInAndActiveTrue(eq(List.of(ExamMode.FULL, ExamMode.BOTH))))
                .thenReturn(List.of(itemFull, itemBoth));

        List<DerailerItemResponse> responses = derailerItemService.getDerailerItems(ExamMode.FULL);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).id()).isEqualTo(10L);
        assertThat(responses.get(0).statementAr()).isEqualTo("عبارة 1");
        assertThat(responses.get(0).derailerTypeId()).isEqualTo(1L);
        assertThat(responses.get(0).responseScaleType()).isEqualTo(ResponseScaleType.FREQUENCY);

        verify(derailerItemRepository).findByExamModeInAndActiveTrue(List.of(ExamMode.FULL, ExamMode.BOTH));
    }

    @Test
    @DisplayName("getDerailerTypes returns taxonomy with indicators")
    void testGetDerailerTypes() {
        DerailerTypeIndicator ind1 = new DerailerTypeIndicator(testType, "مؤشر 1");
        testType.setIndicators(List.of(ind1));

        when(derailerTypeRepository.findAll()).thenReturn(List.of(testType));

        List<DerailerTypeResponse> types = derailerItemService.getDerailerTypes();

        assertThat(types).hasSize(1);
        assertThat(types.get(0).nameAr()).isEqualTo("التحفظ");
        assertThat(types.get(0).indicators()).containsExactly("مؤشر 1");
    }

    @Test
    @DisplayName("Security & Anti-Tampering: DerailerItemResponse DTO strictly excludes idealTarget")
    void testSecurityRule_ZeroKnowledgeDTOPerimeter() {
        Field[] declaredFields = DerailerItemResponse.class.getDeclaredFields();
        List<String> fieldNames = Arrays.stream(declaredFields).map(Field::getName).toList();

        assertThat(fieldNames).containsExactlyInAnyOrder("id", "statementAr", "derailerTypeId", "responseScaleType");
        assertThat(fieldNames).doesNotContain("idealTarget", "target", "riskScore", "isReverseKeyed", "isReverse");
    }
}
