package com.psychometric.platform.features.assessment.repository;

import com.psychometric.platform.features.assessment.domain.model.ExamTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamTypeRepository extends JpaRepository<ExamTypeEntity, Long> {
    Optional<ExamTypeEntity> findByCode(String code);
}
