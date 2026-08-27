package com.psychometric.platform.features.itembank.derailers.service;

import com.psychometric.platform.common.exception.BadRequestException;
import com.psychometric.platform.common.exception.ResourceNotFoundException;
import com.psychometric.platform.features.itembank.derailers.dto.*;
import com.psychometric.platform.features.itembank.derailers.entity.DerailerItem;
import com.psychometric.platform.features.itembank.derailers.entity.DerailerType;
import com.psychometric.platform.features.itembank.derailers.entity.DerailerTypeIndicator;
import com.psychometric.platform.features.itembank.derailers.repository.DerailerItemRepository;
import com.psychometric.platform.features.itembank.derailers.repository.DerailerTypeIndicatorRepository;
import com.psychometric.platform.features.itembank.derailers.repository.DerailerTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminDerailerItemService {

    private static final Logger log = LoggerFactory.getLogger(AdminDerailerItemService.class);

    private final DerailerItemRepository derailerItemRepository;
    private final DerailerTypeRepository derailerTypeRepository;
    private final DerailerTypeIndicatorRepository derailerTypeIndicatorRepository;

    public AdminDerailerItemService(DerailerItemRepository derailerItemRepository,
                                    DerailerTypeRepository derailerTypeRepository,
                                    DerailerTypeIndicatorRepository derailerTypeIndicatorRepository) {
        this.derailerItemRepository = derailerItemRepository;
        this.derailerTypeRepository = derailerTypeRepository;
        this.derailerTypeIndicatorRepository = derailerTypeIndicatorRepository;
    }

    @Transactional
    public DerailerItemAdminResponse create(DerailerItemAdminRequest request) {
        List<DerailerType> derailerTypesList = derailerTypeRepository.findAllById(request.getDerailerTypeIds());
        if (derailerTypesList.size() != request.getDerailerTypeIds().size()) {
            throw new BadRequestException("One or more DerailerTypes not found with provided IDs.");
        }
        Set<DerailerType> derailerTypes = new HashSet<>(derailerTypesList);

        DerailerItem item = new DerailerItem(
                request.getStatementAr(),
                request.getJustificationAr(),
                derailerTypes,
                request.getIdealTarget(),
                request.getResponseScaleType(),
                request.getExamMode()
        );
        DerailerItem saved = derailerItemRepository.save(item);
        log.info("Created DerailerItem with ID: {}", saved.getId());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<DerailerItemAdminResponse> getAll() {
        return derailerItemRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DerailerItemAdminResponse getById(Long id) {
        DerailerItem item = findEntity(id);
        return mapToResponse(item);
    }

    @Transactional
    public DerailerItemAdminResponse update(Long id, DerailerItemAdminRequest request) {
        DerailerItem item = findEntity(id);
        List<DerailerType> derailerTypesList = derailerTypeRepository.findAllById(request.getDerailerTypeIds());
        if (derailerTypesList.size() != request.getDerailerTypeIds().size()) {
            throw new BadRequestException("One or more DerailerTypes not found with provided IDs.");
        }
        Set<DerailerType> derailerTypes = new HashSet<>(derailerTypesList);

        item.setStatementAr(request.getStatementAr());
        item.setJustificationAr(request.getJustificationAr());
        item.setDerailerTypes(derailerTypes);
        item.setIdealTarget(request.getIdealTarget());
        item.setResponseScaleType(request.getResponseScaleType());
        item.setExamMode(request.getExamMode());

        DerailerItem updated = derailerItemRepository.save(item);
        log.info("Updated DerailerItem with ID: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Transactional
    public void softDelete(Long id) {
        DerailerItem item = findEntity(id);
        if (!item.isActive()) {
            derailerItemRepository.delete(item);
            log.info("Permanently deleted disabled DerailerItem with ID: {}", id);
        } else {
            item.setActive(false);
            derailerItemRepository.save(item);
            log.info("Soft-deleted (disabled) DerailerItem with ID: {}", id);
        }
    }

    @Transactional
    public DerailerItemAdminResponse enable(Long id) {
        DerailerItem item = findEntity(id);
        item.setActive(true);
        DerailerItem saved = derailerItemRepository.save(item);
        return mapToResponse(saved);
    }

    @Transactional
    public DerailerItemAdminResponse disable(Long id) {
        DerailerItem item = findEntity(id);
        item.setActive(false);
        DerailerItem saved = derailerItemRepository.save(item);
        return mapToResponse(saved);
    }

    @Transactional
    public DerailerItemAdminResponse reactivate(Long id) {
        return enable(id);
    }

    // Taxonomy methods
    @Transactional(readOnly = true)
    public List<DerailerTypeAdminResponse> getAllTypes() {
        return derailerTypeRepository.findAll().stream()
                .map(t -> new DerailerTypeAdminResponse(
                        t.getId(),
                        t.getNameAr(),
                        t.getDefinitionAr(),
                        t.getIndicators().stream().map(i -> new DerailerTypeIndicatorAdminDto(i.getId(), i.getIndicatorAr())).toList(),
                        derailerItemRepository.findByDerailerTypes_IdAndActiveTrue(t.getId()).size()
                ))
                .toList();
    }

    @Transactional
    public DerailerTypeAdminResponse createType(DerailerTypeAdminRequest request) {
        derailerTypeRepository.findByNameAr(request.getNameAr()).ifPresent(existing -> {
            throw new BadRequestException("DerailerType already exists with name: " + request.getNameAr());
        });

        DerailerType type = new DerailerType(request.getNameAr(), request.getDefinitionAr());
        if (request.getIndicators() != null) {
            for (String ind : request.getIndicators()) {
                type.getIndicators().add(new DerailerTypeIndicator(type, ind));
            }
        }
        DerailerType saved = derailerTypeRepository.save(type);
        List<DerailerTypeIndicatorAdminDto> indicatorDtos = saved.getIndicators().stream()
                .map(i -> new DerailerTypeIndicatorAdminDto(i.getId(), i.getIndicatorAr()))
                .toList();
        return new DerailerTypeAdminResponse(saved.getId(), saved.getNameAr(), saved.getDefinitionAr(), indicatorDtos, 0);
    }

    @Transactional
    public DerailerTypeAdminResponse updateType(Long id, DerailerTypeAdminRequest request) {
        DerailerType type = derailerTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DerailerType not found with ID: " + id));

        derailerTypeRepository.findByNameAr(request.getNameAr()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BadRequestException("DerailerType name already in use: " + request.getNameAr());
            }
        });

        type.setNameAr(request.getNameAr());
        type.setDefinitionAr(request.getDefinitionAr());

        if (request.getIndicators() != null) {
            type.getIndicators().clear();
            for (String ind : request.getIndicators()) {
                type.getIndicators().add(new DerailerTypeIndicator(type, ind));
            }
        }

        DerailerType saved = derailerTypeRepository.save(type);
        List<DerailerTypeIndicatorAdminDto> indicatorDtos = saved.getIndicators().stream()
                .map(i -> new DerailerTypeIndicatorAdminDto(i.getId(), i.getIndicatorAr()))
                .toList();
        long count = derailerItemRepository.findByDerailerTypes_IdAndActiveTrue(saved.getId()).size();
        return new DerailerTypeAdminResponse(saved.getId(), saved.getNameAr(), saved.getDefinitionAr(), indicatorDtos, count);
    }

    private DerailerItem findEntity(Long id) {
        return derailerItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DerailerItem not found with ID: " + id));
    }

    private DerailerItemAdminResponse mapToResponse(DerailerItem item) {
        return new DerailerItemAdminResponse(
                item.getId(),
                item.getStatementAr(),
                item.getJustificationAr(),
                item.getDerailerTypes().stream().map(DerailerType::getId).toList(),
                item.getDerailerTypes().stream().map(DerailerType::getNameAr).toList(),
                item.getIdealTarget(),
                item.getResponseScaleType(),
                item.getExamMode(),
                item.isActive(),
                item.getExposureCount(),
                item.getCreatedAt()
        );
    }
}
