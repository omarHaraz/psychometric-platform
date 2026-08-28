package com.psychometric.platform.features.itembank.sjt.service;

import com.psychometric.platform.common.exception.BadRequestException;
import com.psychometric.platform.common.exception.ResourceNotFoundException;
import com.psychometric.platform.features.itembank.sjt.dto.*;
import com.psychometric.platform.features.itembank.sjt.entity.*;
import com.psychometric.platform.features.itembank.sjt.repository.SjtDomainRepository;
import com.psychometric.platform.features.itembank.sjt.repository.SjtOptionRepository;
import com.psychometric.platform.features.itembank.sjt.repository.SjtScenarioRepository;
import com.psychometric.platform.features.itembank.common.service.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminSjtItemService {

    private static final Logger log = LoggerFactory.getLogger(AdminSjtItemService.class);

    private final SjtScenarioRepository scenarioRepository;
    private final SjtDomainRepository domainRepository;
    private final SjtOptionRepository optionRepository;
    private final CloudinaryService cloudinaryService;

    public AdminSjtItemService(SjtScenarioRepository scenarioRepository,
                               SjtDomainRepository domainRepository,
                               SjtOptionRepository optionRepository,
                               CloudinaryService cloudinaryService) {
        this.scenarioRepository = scenarioRepository;
        this.domainRepository = domainRepository;
        this.optionRepository = optionRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public SjtScenarioAdminResponse create(SjtScenarioAdminRequest request) {
        scenarioRepository.findByItemCode(request.getItemCode()).ifPresent(existing -> {
            throw new BadRequestException("SJT Scenario already exists with itemCode: " + request.getItemCode());
        });

        SjtDomain domain = domainRepository.findById(request.getDomainId())
                .orElseThrow(() -> new BadRequestException("SJT Domain not found with ID: " + request.getDomainId()));

        SjtScenario scenario = new SjtScenario(
                request.getItemCode(),
                domain,
                request.getTitleAr(),
                request.getNarrativeAr(),
                request.getScenarioImageUrl(),
                request.getComplexity(),
                request.getBestOptionKey(),
                request.getRationaleAr(),
                request.getCommonMistakeAr(),
                request.getCoachingNoteAr(),
                request.getExamMode()
        );

        if (request.getOptions() != null) {
            int order = 1;
            for (SjtOptionAdminDto optDto : request.getOptions()) {
                if (optDto.getActionTextAr() != null && !optDto.getActionTextAr().isBlank()) {
                    SjtOption opt = new SjtOption(
                            scenario,
                            optDto.getOptionKey(),
                            optDto.getActionTextAr(),
                            optDto.getEffectivenessScore() != null ? optDto.getEffectivenessScore() : 1.0,
                            optDto.getScoringRationaleAr(),
                            optDto.getOptionKey() == request.getBestOptionKey() || optDto.isBestAction(),
                            order++
                    );
                    scenario.getOptions().add(opt);
                }
            }
        }

        SjtScenario saved = scenarioRepository.save(scenario);
        log.info("Created SJT Scenario with ID: {}, itemCode: {}", saved.getId(), saved.getItemCode());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SjtScenarioAdminResponse> getAll() {
        return scenarioRepository.findAllWithDomainAndOptions().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SjtScenarioAdminResponse getById(Long id) {
        SjtScenario scenario = findEntity(id);
        return mapToResponse(scenario);
    }

    @Transactional
    public SjtScenarioAdminResponse update(Long id, SjtScenarioAdminRequest request) {
        SjtScenario scenario = findEntity(id);

        scenarioRepository.findByItemCode(request.getItemCode()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BadRequestException("ItemCode already in use by another scenario: " + request.getItemCode());
            }
        });

        SjtDomain domain = domainRepository.findById(request.getDomainId())
                .orElseThrow(() -> new BadRequestException("SJT Domain not found with ID: " + request.getDomainId()));

        String oldImageUrl = scenario.getScenarioImageUrl();

        scenario.setItemCode(request.getItemCode());
        scenario.setDomain(domain);
        scenario.setTitleAr(request.getTitleAr());
        scenario.setNarrativeAr(request.getNarrativeAr());
        scenario.setScenarioImageUrl(request.getScenarioImageUrl());
        scenario.setComplexity(request.getComplexity());
        scenario.setBestOptionKey(request.getBestOptionKey());
        scenario.setRationaleAr(request.getRationaleAr());
        scenario.setCommonMistakeAr(request.getCommonMistakeAr());
        scenario.setCoachingNoteAr(request.getCoachingNoteAr());
        scenario.setExamMode(request.getExamMode());

        if (oldImageUrl != null && !oldImageUrl.equals(request.getScenarioImageUrl())) {
            cloudinaryService.deleteImageByUrl(oldImageUrl);
        }

        if (request.getOptions() != null) {
            scenario.getOptions().clear();
            int order = 1;
            for (SjtOptionAdminDto optDto : request.getOptions()) {
                if (optDto.getActionTextAr() != null && !optDto.getActionTextAr().isBlank()) {
                    SjtOption opt = new SjtOption(
                            scenario,
                            optDto.getOptionKey(),
                            optDto.getActionTextAr(),
                            optDto.getEffectivenessScore() != null ? optDto.getEffectivenessScore() : 1.0,
                            optDto.getScoringRationaleAr(),
                            optDto.getOptionKey() == request.getBestOptionKey() || optDto.isBestAction(),
                            order++
                    );
                    scenario.getOptions().add(opt);
                }
            }
        }

        SjtScenario updated = scenarioRepository.save(scenario);
        log.info("Updated SJT Scenario with ID: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Transactional
    public void softDelete(Long id) {
        SjtScenario scenario = findEntity(id);
        if (!scenario.isActive()) {
            if (scenario.getScenarioImageUrl() != null) {
                cloudinaryService.deleteImageByUrl(scenario.getScenarioImageUrl());
            }
            scenarioRepository.delete(scenario);
            log.info("Permanently deleted disabled SJT Scenario with ID: {}", id);
        } else {
            scenario.setActive(false);
            scenarioRepository.save(scenario);
            log.info("Soft-deleted (disabled) SJT Scenario with ID: {}", id);
        }
    }

    @Transactional
    public SjtScenarioAdminResponse enable(Long id) {
        SjtScenario scenario = findEntity(id);
        scenario.setActive(true);
        SjtScenario saved = scenarioRepository.save(scenario);
        return mapToResponse(saved);
    }

    @Transactional
    public SjtScenarioAdminResponse disable(Long id) {
        SjtScenario scenario = findEntity(id);
        scenario.setActive(false);
        SjtScenario saved = scenarioRepository.save(scenario);
        return mapToResponse(saved);
    }

    @Transactional
    public SjtScenarioAdminResponse reactivate(Long id) {
        return enable(id);
    }

    // Domain Taxonomy Admin methods
    @Transactional(readOnly = true)
    public List<SjtDomainAdminResponse> getAllDomains() {
        return domainRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(d -> new SjtDomainAdminResponse(
                        d.getId(),
                        d.getCode(),
                        d.getNameAr(),
                        d.getDescriptionAr(),
                        d.getDisplayOrder(),
                        scenarioRepository.findByDomain_IdAndActiveTrue(d.getId()).size()
                ))
                .toList();
    }

    @Transactional
    public SjtDomainAdminResponse createDomain(SjtDomainAdminRequest request) {
        domainRepository.findByCode(request.getCode()).ifPresent(existing -> {
            throw new BadRequestException("SJT Domain already exists with code: " + request.getCode());
        });

        SjtDomain domain = new SjtDomain(
                request.getCode(),
                request.getNameAr(),
                request.getDescriptionAr(),
                request.getDisplayOrder()
        );
        SjtDomain saved = domainRepository.save(domain);
        return new SjtDomainAdminResponse(saved.getId(), saved.getCode(), saved.getNameAr(), saved.getDescriptionAr(), saved.getDisplayOrder(), 0);
    }

    @Transactional
    public SjtDomainAdminResponse updateDomain(Long id, SjtDomainAdminRequest request) {
        SjtDomain domain = domainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SJT Domain not found with ID: " + id));

        domainRepository.findByCode(request.getCode()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BadRequestException("SJT Domain code already in use: " + request.getCode());
            }
        });

        domain.setCode(request.getCode());
        domain.setNameAr(request.getNameAr());
        domain.setDescriptionAr(request.getDescriptionAr());
        domain.setDisplayOrder(request.getDisplayOrder());

        SjtDomain saved = domainRepository.save(domain);
        long count = scenarioRepository.findByDomain_IdAndActiveTrue(saved.getId()).size();
        return new SjtDomainAdminResponse(saved.getId(), saved.getCode(), saved.getNameAr(), saved.getDescriptionAr(), saved.getDisplayOrder(), count);
    }

    private SjtScenario findEntity(Long id) {
        return scenarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SJT Scenario not found with ID: " + id));
    }

    private SjtScenarioAdminResponse mapToResponse(SjtScenario s) {
        List<SjtOptionAdminDto> optionDtos = s.getOptions().stream()
                .map(opt -> new SjtOptionAdminDto(
                        opt.getId(),
                        opt.getOptionKey(),
                        opt.getActionTextAr(),
                        opt.getEffectivenessScore(),
                        opt.getScoringRationaleAr(),
                        opt.isBestAction()
                ))
                .toList();

        return new SjtScenarioAdminResponse(
                s.getId(),
                s.getItemCode(),
                s.getDomain() != null ? s.getDomain().getId() : null,
                s.getDomain() != null ? s.getDomain().getNameAr() : null,
                s.getTitleAr(),
                s.getNarrativeAr(),
                s.getScenarioImageUrl(),
                s.getComplexity(),
                s.getBestOptionKey(),
                s.getRationaleAr(),
                s.getCommonMistakeAr(),
                s.getCoachingNoteAr(),
                s.getExamMode(),
                s.isActive(),
                s.getExposureCount(),
                s.getCreatedAt(),
                optionDtos
        );
    }
}
