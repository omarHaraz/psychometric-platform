package com.psychometric.platform.features.itembank.personality.repository;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.personality.entity.PersonalityItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PersonalityItemRepository extends JpaRepository<PersonalityItem, Long> {

    List<PersonalityItem> findByExamModeInAndActiveTrue(Collection<ExamMode> examModes);

    List<PersonalityItem> findByCompetencies_IdAndActiveTrue(Long competencyId);

    @Query("SELECT p FROM PersonalityItem p JOIN p.competencies c WHERE c.id = :competencyId AND p.examMode IN :modes AND p.active = true")
    List<PersonalityItem> findByCompetenciesAndModes(
            @Param("competencyId") Long competencyId,
            @Param("modes") Collection<ExamMode> modes
    );
}
