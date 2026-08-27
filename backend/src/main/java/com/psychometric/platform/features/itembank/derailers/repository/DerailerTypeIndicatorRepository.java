package com.psychometric.platform.features.itembank.derailers.repository;

import com.psychometric.platform.features.itembank.derailers.entity.DerailerTypeIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DerailerTypeIndicatorRepository extends JpaRepository<DerailerTypeIndicator, Long> {
    List<DerailerTypeIndicator> findByDerailerType_Id(Long derailerTypeId);
}
