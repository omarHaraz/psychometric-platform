package com.psychometric.platform.features.itembank.sjt.repository;

import com.psychometric.platform.features.itembank.sjt.entity.SjtOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SjtOptionRepository extends JpaRepository<SjtOption, Long> {
    List<SjtOption> findByScenario_Id(Long scenarioId);
}
