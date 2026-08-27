package com.psychometric.platform.features.itembank.sjt.service;

import com.psychometric.platform.common.exception.ResourceNotFoundException;
import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.sjt.dto.SjtDomainResponse;
import com.psychometric.platform.features.itembank.sjt.dto.SjtOptionResponse;
import com.psychometric.platform.features.itembank.sjt.dto.SjtScenarioResponse;
import com.psychometric.platform.features.itembank.sjt.entity.SjtOption;
import com.psychometric.platform.features.itembank.sjt.entity.SjtScenario;
import com.psychometric.platform.features.itembank.sjt.repository.SjtDomainRepository;
import com.psychometric.platform.features.itembank.sjt.repository.SjtScenarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SjtService {

    private static final Logger log = LoggerFactory.getLogger(SjtService.class);

    private final SjtDomainRepository sjtDomainRepository;
    private final SjtScenarioRepository sjtScenarioRepository;

    public SjtService(SjtDomainRepository sjtDomainRepository, SjtScenarioRepository sjtScenarioRepository) {
        this.sjtDomainRepository = sjtDomainRepository;
        this.sjtScenarioRepository = sjtScenarioRepository;
    }

    @Transactional(readOnly = true)
    public List<SjtDomainResponse> getAllDomains() {
        return sjtDomainRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(d -> new SjtDomainResponse(
                        d.getId(),
                        d.getCode(),
                        d.getNameAr(),
                        d.getDescriptionAr(),
                        d.getDisplayOrder()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SjtScenarioResponse> getCandidateScenarios(Long domainId, ExamMode examMode) {
        log.debug("Fetching SJT scenarios for domainId: {}, mode: {}", domainId, examMode);

        List<ExamMode> modes = switch (examMode) {
            case QUICK -> List.of(ExamMode.QUICK, ExamMode.BOTH);
            case FULL -> List.of(ExamMode.FULL, ExamMode.BOTH);
            case BOTH -> List.of(ExamMode.BOTH);
            case null -> List.of(ExamMode.FULL, ExamMode.BOTH);
        };

        List<SjtScenario> scenarios;
        if (domainId != null) {
            scenarios = sjtScenarioRepository.findActiveByDomainAndExamModeWithOptions(domainId, modes);
        } else {
            scenarios = sjtScenarioRepository.findActiveByExamModeWithOptions(modes);
        }

        return scenarios.stream()
                .map(this::mapToScenarioResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SjtScenarioResponse getScenarioById(Long id) {
        SjtScenario scenario = sjtScenarioRepository.findByIdWithOptions(id)
                .orElseThrow(() -> new ResourceNotFoundException("SJT Scenario not found with ID: " + id));
        return mapToScenarioResponse(scenario);
    }

    private SjtScenarioResponse mapToScenarioResponse(SjtScenario s) {
        List<SjtOptionResponse> optionDtos = s.getOptions().stream()
                .map(this::mapOptionResponse)
                .toList();

        return new SjtScenarioResponse(
                s.getId(),
                s.getItemCode(),
                s.getDomain() != null ? s.getDomain().getId() : null,
                s.getTitleAr(),
                s.getNarrativeAr(),
                s.getScenarioImageUrl(),
                s.getComplexity(),
                optionDtos
        );
    }

    private SjtOptionResponse mapOptionResponse(SjtOption opt) {
        return new SjtOptionResponse(
                opt.getId(),
                opt.getOptionKey(),
                opt.getActionTextAr()
        );
    }
}
