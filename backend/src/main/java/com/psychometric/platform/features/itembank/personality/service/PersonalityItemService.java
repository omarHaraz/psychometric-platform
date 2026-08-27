package com.psychometric.platform.features.itembank.personality.service;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.personality.dto.PersonalityItemResponse;
import com.psychometric.platform.features.itembank.personality.entity.PersonalityItem;
import com.psychometric.platform.features.itembank.personality.repository.PersonalityItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonalityItemService {

    private static final Logger log = LoggerFactory.getLogger(PersonalityItemService.class);

    private final PersonalityItemRepository personalityItemRepository;

    public PersonalityItemService(PersonalityItemRepository personalityItemRepository) {
        this.personalityItemRepository = personalityItemRepository;
    }

    @Transactional(readOnly = true)
    public List<PersonalityItemResponse> getPersonalityItems(ExamMode examMode) {
        log.debug("Fetching personality items for exam mode: {}", examMode);

        List<ExamMode> modes = switch (examMode) {
            case QUICK -> List.of(ExamMode.QUICK, ExamMode.BOTH);
            case FULL -> List.of(ExamMode.FULL, ExamMode.BOTH);
            case BOTH -> List.of(ExamMode.BOTH);
            case null -> List.of(ExamMode.QUICK, ExamMode.BOTH);
        };

        List<PersonalityItem> items = personalityItemRepository.findByExamModeInAndActiveTrue(modes);
        return items.stream()
                .map(item -> new PersonalityItemResponse(
                        item.getId(),
                        item.getStatementAr(),
                        item.getCompetencies().stream().map(com.psychometric.platform.features.itembank.personality.entity.Competency::getId).toList()
                ))
                .toList();
    }
}
