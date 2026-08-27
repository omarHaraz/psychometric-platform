package com.psychometric.platform.features.itembank.gcat.service;

import com.psychometric.platform.common.exception.ResourceNotFoundException;
import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.gcat.dto.GcatOptionCandidateDto;
import com.psychometric.platform.features.itembank.gcat.dto.GcatQuestionCandidateDto;
import com.psychometric.platform.features.itembank.gcat.dto.GcatSubtestResponse;
import com.psychometric.platform.features.itembank.gcat.entity.GcatOption;
import com.psychometric.platform.features.itembank.gcat.entity.GcatQuestion;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import com.psychometric.platform.features.itembank.gcat.repository.GcatQuestionRepository;
import com.psychometric.platform.features.itembank.gcat.repository.GcatSubtestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GcatService {

    private static final Logger log = LoggerFactory.getLogger(GcatService.class);

    private final GcatSubtestRepository subtestRepository;
    private final GcatQuestionRepository questionRepository;

    public GcatService(GcatSubtestRepository subtestRepository, GcatQuestionRepository questionRepository) {
        this.subtestRepository = subtestRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional(readOnly = true)
    public List<GcatSubtestResponse> getAllSubtests() {
        return subtestRepository.findAll().stream()
                .map(s -> new GcatSubtestResponse(
                        s.getId(),
                        s.getCode(),
                        s.getNameAr(),
                        s.getDescriptionAr(),
                        s.getFullModeQuota(),
                        s.getQuickModeQuota(),
                        s.getTimeLimitSeconds()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GcatQuestionCandidateDto> getCandidateQuestions(GcatSubtestCode subtestCode, ExamMode examMode) {
        log.debug("Fetching GCAT questions for subtest: {}, mode: {}", subtestCode, examMode);

        List<ExamMode> modes = switch (examMode) {
            case QUICK -> List.of(ExamMode.QUICK, ExamMode.BOTH);
            case FULL -> List.of(ExamMode.FULL, ExamMode.BOTH);
            case BOTH -> List.of(ExamMode.BOTH);
            case null -> List.of(ExamMode.FULL, ExamMode.BOTH);
        };

        List<GcatQuestion> questions;
        if (subtestCode != null) {
            questions = questionRepository.findActiveBySubtestAndExamModeWithOptionCandidates(subtestCode, modes);
        } else {
            questions = questionRepository.findActiveByExamModeWithOptionCandidates(modes);
        }

        return questions.stream()
                .map(this::mapToCandidateDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public GcatQuestionCandidateDto getQuestionById(Long id) {
        GcatQuestion q = questionRepository.findByIdWithOptions(id)
                .orElseThrow(() -> new ResourceNotFoundException("GCAT Question not found with ID: " + id));
        return mapToCandidateDto(q);
    }

    private GcatQuestionCandidateDto mapToCandidateDto(GcatQuestion q) {
        List<GcatOptionCandidateDto> optionDtos = q.getOptions().stream()
                .map(this::mapOptionCandidateDto)
                .toList();

        return new GcatQuestionCandidateDto(
                q.getId(),
                q.getItemCode(),
                q.getSubtest() != null ? q.getSubtest().getCode() : null,
                q.getTitleAr(),
                q.getPromptTextAr(),
                q.getQuestionImageUrl(),
                q.getDifficulty(),
                optionDtos
        );
    }

    private GcatOptionCandidateDto mapOptionCandidateDto(GcatOption opt) {
        return new GcatOptionCandidateDto(
                opt.getId(),
                opt.getOptionKey(),
                opt.getOptionTextAr(),
                opt.getOptionImageUrl()
        );
    }
}
