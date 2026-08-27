package com.psychometric.platform.features.itembank.sjt.repository;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.sjt.entity.SjtScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SjtScenarioRepository extends JpaRepository<SjtScenario, Long> {

    Optional<SjtScenario> findByItemCode(String itemCode);

    List<SjtScenario> findByDomain_IdAndExamModeInAndActiveTrue(Long domainId, Collection<ExamMode> examModes);

    List<SjtScenario> findByExamModeInAndActiveTrue(Collection<ExamMode> examModes);

    List<SjtScenario> findByDomain_IdAndActiveTrue(Long domainId);

    @Query("SELECT DISTINCT s FROM SjtScenario s LEFT JOIN FETCH s.domain LEFT JOIN FETCH s.options ORDER BY s.id ASC")
    List<SjtScenario> findAllWithDomainAndOptions();

    @Query("SELECT DISTINCT s FROM SjtScenario s LEFT JOIN FETCH s.domain LEFT JOIN FETCH s.options WHERE s.id = :id")
    Optional<SjtScenario> findByIdWithOptions(@Param("id") Long id);

    @Query("SELECT DISTINCT s FROM SjtScenario s LEFT JOIN FETCH s.domain LEFT JOIN FETCH s.options WHERE s.domain.id = :domainId AND s.examMode IN :modes AND s.active = true")
    List<SjtScenario> findActiveByDomainAndExamModeWithOptions(
            @Param("domainId") Long domainId,
            @Param("modes") Collection<ExamMode> modes
    );

    @Query("SELECT DISTINCT s FROM SjtScenario s LEFT JOIN FETCH s.domain LEFT JOIN FETCH s.options WHERE s.examMode IN :modes AND s.active = true")
    List<SjtScenario> findActiveByExamModeWithOptions(@Param("modes") Collection<ExamMode> modes);
}
