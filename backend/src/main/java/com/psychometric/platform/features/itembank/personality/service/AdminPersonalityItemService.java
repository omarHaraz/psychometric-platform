package com.psychometric.platform.features.itembank.personality.service;

import com.psychometric.platform.common.exception.BadRequestException;
import com.psychometric.platform.common.exception.ResourceNotFoundException;
import com.psychometric.platform.features.itembank.personality.dto.PersonalityItemAdminRequest;
import com.psychometric.platform.features.itembank.personality.dto.PersonalityItemAdminResponse;
import com.psychometric.platform.features.itembank.personality.entity.Competency;
import com.psychometric.platform.features.itembank.personality.entity.PersonalityItem;
import com.psychometric.platform.features.itembank.personality.repository.CompetencyRepository;
import com.psychometric.platform.features.itembank.personality.repository.PersonalityItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminPersonalityItemService {

    private static final Logger log = LoggerFactory.getLogger(AdminPersonalityItemService.class);

    private final PersonalityItemRepository personalityItemRepository;
    private final CompetencyRepository competencyRepository;

    public AdminPersonalityItemService(PersonalityItemRepository personalityItemRepository, CompetencyRepository competencyRepository) {
        this.personalityItemRepository = personalityItemRepository;
        this.competencyRepository = competencyRepository;
    }

    @Transactional
    public PersonalityItemAdminResponse create(PersonalityItemAdminRequest request) {
        List<Competency> competencies = competencyRepository.findAllById(request.getCompetencyIds());
        if (competencies.isEmpty()) {
            throw new BadRequestException("No valid competencies found for provided IDs");
        }

        PersonalityItem item = new PersonalityItem(
                request.getStatementAr(),
                new java.util.HashSet<>(competencies),
                request.getIdealTarget(),
                request.getExamMode(),
                request.getJustificationAr()
        );
        PersonalityItem saved = personalityItemRepository.save(item);
        log.info("Created PersonalityItem with ID: {}", saved.getId());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PersonalityItemAdminResponse> getAll() {
        return personalityItemRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PersonalityItemAdminResponse getById(Long id) {
        PersonalityItem item = findEntity(id);
        return mapToResponse(item);
    }

    @Transactional
    public PersonalityItemAdminResponse update(Long id, PersonalityItemAdminRequest request) {
        PersonalityItem item = findEntity(id);
        List<Competency> competencies = competencyRepository.findAllById(request.getCompetencyIds());
        if (competencies.isEmpty()) {
            throw new BadRequestException("No valid competencies found for provided IDs");
        }

        item.setStatementAr(request.getStatementAr());
        item.setCompetencies(new java.util.HashSet<>(competencies));
        item.setIdealTarget(request.getIdealTarget());
        item.setExamMode(request.getExamMode());
        item.setJustificationAr(request.getJustificationAr());

        PersonalityItem updated = personalityItemRepository.save(item);
        log.info("Updated PersonalityItem with ID: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Transactional
    public void softDelete(Long id) {
        PersonalityItem item = findEntity(id);
        if (!item.isActive()) {
            personalityItemRepository.delete(item);
            log.info("Permanently deleted disabled PersonalityItem with ID: {}", id);
        } else {
            item.setActive(false);
            personalityItemRepository.save(item);
            log.info("Soft-deleted (disabled) PersonalityItem with ID: {}", id);
        }
    }

    @Transactional
    public PersonalityItemAdminResponse enable(Long id) {
        PersonalityItem item = findEntity(id);
        item.setActive(true);
        PersonalityItem saved = personalityItemRepository.save(item);
        return mapToResponse(saved);
    }

    @Transactional
    public PersonalityItemAdminResponse disable(Long id) {
        PersonalityItem item = findEntity(id);
        item.setActive(false);
        PersonalityItem saved = personalityItemRepository.save(item);
        return mapToResponse(saved);
    }

    @Transactional
    public PersonalityItemAdminResponse reactivate(Long id) {
        return enable(id);
    }

    private PersonalityItem findEntity(Long id) {
        return personalityItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PersonalityItem not found with ID: " + id));
    }

    private PersonalityItemAdminResponse mapToResponse(PersonalityItem item) {
        return new PersonalityItemAdminResponse(
                item.getId(),
                item.getStatementAr(),
                item.getCompetencies().stream().map(Competency::getId).toList(),
                item.getCompetencies().stream().map(Competency::getNameAr).toList(),
                item.getIdealTarget(),
                item.getExamMode(),
                item.isActive(),
                item.getExposureCount(),
                item.getCreatedAt(),
                item.getJustificationAr()
        );
    }
}
