package com.psychometric.platform.features.itembank.personality.service;

import com.psychometric.platform.common.exception.BadRequestException;
import com.psychometric.platform.common.exception.ResourceNotFoundException;
import com.psychometric.platform.features.itembank.personality.dto.CompetencyAdminRequest;
import com.psychometric.platform.features.itembank.personality.dto.CompetencyAdminResponse;
import com.psychometric.platform.features.itembank.personality.entity.Competency;
import com.psychometric.platform.features.itembank.personality.repository.CompetencyRepository;
import com.psychometric.platform.features.itembank.personality.repository.PersonalityItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminTaxonomyService {

    private static final Logger log = LoggerFactory.getLogger(AdminTaxonomyService.class);

    private final CompetencyRepository competencyRepository;
    private final PersonalityItemRepository personalityItemRepository;

    public AdminTaxonomyService(CompetencyRepository competencyRepository, PersonalityItemRepository personalityItemRepository) {
        this.competencyRepository = competencyRepository;
        this.personalityItemRepository = personalityItemRepository;
    }

    @Transactional(readOnly = true)
    public List<CompetencyAdminResponse> getAllCompetencies() {
        return competencyRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(c -> new CompetencyAdminResponse(
                        c.getId(),
                        c.getCode(),
                        c.getNameAr(),
                        c.getDefinitionAr(),
                        c.getDisplayOrder(),
                        personalityItemRepository.findByCompetencies_IdAndActiveTrue(c.getId()).size()
                ))
                .toList();
    }

    @Transactional
    public CompetencyAdminResponse createCompetency(CompetencyAdminRequest request) {
        if (competencyRepository.findByCode(request.getCode()).isPresent()) {
            throw new BadRequestException("Competency code already exists: " + request.getCode());
        }
        Competency comp = new Competency(
                request.getCode(),
                request.getNameAr(),
                request.getDefinitionAr(),
                request.getDisplayOrder()
        );
        Competency saved = competencyRepository.save(comp);
        return new CompetencyAdminResponse(saved.getId(), saved.getCode(), saved.getNameAr(), saved.getDefinitionAr(), saved.getDisplayOrder(), 0);
    }

    @Transactional
    public CompetencyAdminResponse updateCompetency(Long id, CompetencyAdminRequest request) {
        Competency comp = competencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competency not found with ID: " + id));

        competencyRepository.findByCode(request.getCode()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BadRequestException("Competency code already in use: " + request.getCode());
            }
        });

        comp.setCode(request.getCode());
        comp.setNameAr(request.getNameAr());
        comp.setDefinitionAr(request.getDefinitionAr());
        comp.setDisplayOrder(request.getDisplayOrder());

        Competency saved = competencyRepository.save(comp);
        long count = personalityItemRepository.findByCompetencies_IdAndActiveTrue(saved.getId()).size();
        return new CompetencyAdminResponse(saved.getId(), saved.getCode(), saved.getNameAr(), saved.getDefinitionAr(), saved.getDisplayOrder(), count);
    }
}
