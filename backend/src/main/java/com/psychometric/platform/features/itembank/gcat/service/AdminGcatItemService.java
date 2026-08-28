package com.psychometric.platform.features.itembank.gcat.service;

import com.psychometric.platform.common.exception.BadRequestException;
import com.psychometric.platform.common.exception.ResourceNotFoundException;
import com.psychometric.platform.features.itembank.gcat.dto.*;
import com.psychometric.platform.features.itembank.gcat.entity.*;
import com.psychometric.platform.features.itembank.gcat.repository.GcatOptionRepository;
import com.psychometric.platform.features.itembank.gcat.repository.GcatQuestionRepository;
import com.psychometric.platform.features.itembank.gcat.repository.GcatSubtestRepository;
import com.psychometric.platform.features.itembank.common.service.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminGcatItemService {

    private static final Logger log = LoggerFactory.getLogger(AdminGcatItemService.class);

    private final GcatQuestionRepository questionRepository;
    private final GcatSubtestRepository subtestRepository;
    private final GcatOptionRepository optionRepository;
    private final CloudinaryService cloudinaryService;

    public AdminGcatItemService(GcatQuestionRepository questionRepository,
                                GcatSubtestRepository subtestRepository,
                                GcatOptionRepository optionRepository,
                                CloudinaryService cloudinaryService) {
        this.questionRepository = questionRepository;
        this.subtestRepository = subtestRepository;
        this.optionRepository = optionRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public GcatQuestionAdminResponse create(GcatQuestionAdminRequest request) {
        questionRepository.findByItemCode(request.getItemCode()).ifPresent(existing -> {
            throw new BadRequestException("GCAT Question already exists with itemCode: " + request.getItemCode());
        });

        GcatSubtest subtest = subtestRepository.findByCode(request.getSubtestCode())
                .orElseThrow(() -> new BadRequestException("GcatSubtest not found with code: " + request.getSubtestCode()));

        GcatQuestion question = new GcatQuestion(
                request.getItemCode(),
                subtest,
                request.getTitleAr(),
                request.getPromptTextAr(),
                request.getQuestionImageUrl(),
                request.getPatternTypeAr(),
                request.getObservationAr(),
                request.getRuleAr(),
                request.getApplicationAr(),
                request.getCorrectOptionKey(),
                request.getDifficulty(),
                request.getExamMode()
        );
        question.setQuestionImagePublicId(request.getQuestionImagePublicId());

        if (request.getOptions() != null) {
            int order = 1;
            for (GcatOptionAdminDto optDto : request.getOptions()) {
                boolean hasText = optDto.getOptionTextAr() != null && !optDto.getOptionTextAr().isBlank();
                boolean hasImage = optDto.getOptionImageUrl() != null && !optDto.getOptionImageUrl().isBlank();
                if (hasText || hasImage) {
                    GcatOption opt = new GcatOption(
                            question,
                            optDto.getOptionKey(),
                            optDto.getOptionTextAr(),
                            optDto.getOptionImageUrl(),
                            optDto.getOptionKey() == request.getCorrectOptionKey() || optDto.isCorrect(),
                            order++
                    );
                    question.getOptions().add(opt);
                }
            }
        }

        GcatQuestion saved = questionRepository.save(question);
        log.info("Created GCAT Question with ID: {}, itemCode: {}", saved.getId(), saved.getItemCode());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<GcatQuestionAdminResponse> getAll() {
        return questionRepository.findAllWithSubtestAndOptions().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GcatQuestionAdminResponse getById(Long id) {
        GcatQuestion question = findEntity(id);
        return mapToResponse(question);
    }

    @Transactional
    public GcatQuestionAdminResponse update(Long id, GcatQuestionAdminRequest request) {
        GcatQuestion question = findEntity(id);

        questionRepository.findByItemCode(request.getItemCode()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BadRequestException("ItemCode already in use by another question: " + request.getItemCode());
            }
        });

        GcatSubtest subtest = subtestRepository.findByCode(request.getSubtestCode())
                .orElseThrow(() -> new BadRequestException("GCAT Subtest not found for code: " + request.getSubtestCode()));

        String oldImageUrl = question.getQuestionImageUrl();

        question.setItemCode(request.getItemCode());
        question.setSubtest(subtest);
        question.setTitleAr(request.getTitleAr());
        question.setPromptTextAr(request.getPromptTextAr());
        question.setQuestionImageUrl(request.getQuestionImageUrl());
        question.setQuestionImagePublicId(request.getQuestionImagePublicId());
        question.setPatternTypeAr(request.getPatternTypeAr());
        question.setObservationAr(request.getObservationAr());
        question.setRuleAr(request.getRuleAr());
        question.setApplicationAr(request.getApplicationAr());
        question.setCorrectOptionKey(request.getCorrectOptionKey());
        question.setDifficulty(request.getDifficulty());
        question.setExamMode(request.getExamMode());

        if (oldImageUrl != null && !oldImageUrl.equals(request.getQuestionImageUrl())) {
            cloudinaryService.deleteImageByUrl(oldImageUrl);
        }

        if (request.getOptions() != null) {
            for (GcatOption oldOpt : question.getOptions()) {
                if (oldOpt.getOptionImageUrl() != null) {
                    boolean kept = request.getOptions().stream().anyMatch(o -> oldOpt.getOptionImageUrl().equals(o.getOptionImageUrl()));
                    if (!kept) {
                        cloudinaryService.deleteImageByUrl(oldOpt.getOptionImageUrl());
                    }
                }
            }
            question.getOptions().clear();
            int order = 1;
            for (GcatOptionAdminDto optDto : request.getOptions()) {
                boolean hasText = optDto.getOptionTextAr() != null && !optDto.getOptionTextAr().isBlank();
                boolean hasImage = optDto.getOptionImageUrl() != null && !optDto.getOptionImageUrl().isBlank();
                if (hasText || hasImage) {
                    GcatOption opt = new GcatOption(
                            question,
                            optDto.getOptionKey(),
                            optDto.getOptionTextAr(),
                            optDto.getOptionImageUrl(),
                            optDto.getOptionKey() == request.getCorrectOptionKey() || optDto.isCorrect(),
                            order++
                    );
                    question.getOptions().add(opt);
                }
            }
        }

        GcatQuestion updated = questionRepository.save(question);
        log.info("Updated GCAT Question with ID: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Transactional
    public void softDelete(Long id) {
        GcatQuestion question = findEntity(id);
        if (!question.isActive()) {
            if (question.getQuestionImageUrl() != null) {
                cloudinaryService.deleteImageByUrl(question.getQuestionImageUrl());
            }
            for (GcatOption opt : question.getOptions()) {
                if (opt.getOptionImageUrl() != null) {
                    cloudinaryService.deleteImageByUrl(opt.getOptionImageUrl());
                }
            }
            questionRepository.delete(question);
            log.info("Permanently deleted disabled GCAT Question with ID: {}", id);
        } else {
            question.setActive(false);
            questionRepository.save(question);
            log.info("Soft-deleted (disabled) GCAT Question with ID: {}", id);
        }
    }

    @Transactional
    public GcatQuestionAdminResponse enable(Long id) {
        GcatQuestion question = findEntity(id);
        question.setActive(true);
        GcatQuestion saved = questionRepository.save(question);
        return mapToResponse(saved);
    }

    @Transactional
    public GcatQuestionAdminResponse disable(Long id) {
        GcatQuestion question = findEntity(id);
        question.setActive(false);
        GcatQuestion saved = questionRepository.save(question);
        return mapToResponse(saved);
    }

    @Transactional
    public GcatQuestionAdminResponse reactivate(Long id) {
        return enable(id);
    }

    // Subtest Taxonomy Admin methods
    @Transactional(readOnly = true)
    public List<GcatSubtestAdminResponse> getAllSubtests() {
        return subtestRepository.findAll().stream()
                .map(s -> new GcatSubtestAdminResponse(
                        s.getId(),
                        s.getCode(),
                        s.getNameAr(),
                        s.getDescriptionAr(),
                        s.getFullModeQuota(),
                        s.getQuickModeQuota(),
                        s.getTimeLimitSeconds(),
                        questionRepository.findBySubtest_IdAndActiveTrue(s.getId()).size()
                ))
                .toList();
    }

    @Transactional
    public GcatSubtestAdminResponse updateSubtest(Long id, GcatSubtestAdminRequest request) {
        GcatSubtest subtest = subtestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GcatSubtest not found with ID: " + id));

        subtest.setNameAr(request.getNameAr());
        subtest.setDescriptionAr(request.getDescriptionAr());
        subtest.setFullModeQuota(request.getFullModeQuota());
        subtest.setQuickModeQuota(request.getQuickModeQuota());
        subtest.setTimeLimitSeconds(request.getTimeLimitSeconds());

        GcatSubtest saved = subtestRepository.save(subtest);
        long count = questionRepository.findBySubtest_IdAndActiveTrue(saved.getId()).size();
        return new GcatSubtestAdminResponse(saved.getId(), saved.getCode(), saved.getNameAr(), saved.getDescriptionAr(), saved.getFullModeQuota(), saved.getQuickModeQuota(), saved.getTimeLimitSeconds(), count);
    }

    private GcatQuestion findEntity(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GCAT Question not found with ID: " + id));
    }

    private GcatQuestionAdminResponse mapToResponse(GcatQuestion q) {
        List<GcatOptionAdminDto> optionDtos = q.getOptions().stream()
                .map(opt -> new GcatOptionAdminDto(
                        opt.getId(),
                        opt.getOptionKey(),
                        opt.getOptionTextAr(),
                        opt.getOptionImageUrl(),
                        opt.isCorrect()
                ))
                .toList();

        return new GcatQuestionAdminResponse(
                q.getId(),
                q.getItemCode(),
                q.getSubtest() != null ? q.getSubtest().getCode() : null,
                q.getTitleAr(),
                q.getPromptTextAr(),
                q.getQuestionImageUrl(),
                q.getQuestionImagePublicId(),
                q.getPatternTypeAr(),
                q.getObservationAr(),
                q.getRuleAr(),
                q.getApplicationAr(),
                q.getCorrectOptionKey(),
                q.getDifficulty(),
                q.getExamMode(),
                q.isActive(),
                q.getExposureCount(),
                q.getCreatedAt(),
                optionDtos
        );
    }
}
