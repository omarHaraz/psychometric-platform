package com.psychometric.platform.features.itembank.derailers.service;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerItemResponse;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerTypeResponse;
import com.psychometric.platform.features.itembank.derailers.entity.DerailerItem;
import com.psychometric.platform.features.itembank.derailers.entity.DerailerType;
import com.psychometric.platform.features.itembank.derailers.entity.DerailerTypeIndicator;
import com.psychometric.platform.features.itembank.derailers.repository.DerailerItemRepository;
import com.psychometric.platform.features.itembank.derailers.repository.DerailerTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DerailerItemService {

    private static final Logger log = LoggerFactory.getLogger(DerailerItemService.class);

    private final DerailerItemRepository derailerItemRepository;
    private final DerailerTypeRepository derailerTypeRepository;

    public DerailerItemService(DerailerItemRepository derailerItemRepository, DerailerTypeRepository derailerTypeRepository) {
        this.derailerItemRepository = derailerItemRepository;
        this.derailerTypeRepository = derailerTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<DerailerItemResponse> getDerailerItems(ExamMode examMode) {
        log.debug("Fetching derailer items for exam mode: {}", examMode);

        List<ExamMode> modes = switch (examMode) {
            case QUICK -> List.of(ExamMode.QUICK, ExamMode.BOTH);
            case FULL -> List.of(ExamMode.FULL, ExamMode.BOTH);
            case BOTH -> List.of(ExamMode.BOTH);
            case null -> List.of(ExamMode.FULL, ExamMode.BOTH);
        };

        List<DerailerItem> items = derailerItemRepository.findByExamModeInAndActiveTrue(modes);
        return items.stream()
                .map(item -> new DerailerItemResponse(
                        item.getId(),
                        item.getStatementAr(),
                        item.getDerailerTypes().stream().map(DerailerType::getId).toList(),
                        item.getResponseScaleType()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DerailerTypeResponse> getDerailerTypes() {
        List<DerailerType> types = derailerTypeRepository.findAll();
        return types.stream()
                .map(type -> new DerailerTypeResponse(
                        type.getId(),
                        type.getNameAr(),
                        type.getDefinitionAr(),
                        type.getIndicators().stream().map(DerailerTypeIndicator::getIndicatorAr).toList()
                ))
                .toList();
    }
}
