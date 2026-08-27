package com.psychometric.platform.features.itembank.derailers.repository;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.derailers.entity.DerailerItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DerailerItemRepository extends JpaRepository<DerailerItem, Long> {

    List<DerailerItem> findByExamModeInAndActiveTrue(Collection<ExamMode> examModes);

    List<DerailerItem> findByDerailerTypes_IdAndActiveTrue(Long derailerTypeId);

    @Query("SELECT d FROM DerailerItem d JOIN d.derailerTypes dt WHERE dt.id = :typeId AND d.examMode IN :modes AND d.active = true")
    List<DerailerItem> findByTypeAndModes(
            @Param("typeId") Long typeId,
            @Param("modes") Collection<ExamMode> modes
    );
}
