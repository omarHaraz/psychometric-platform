package com.psychometric.platform.features.itembank.derailers.repository;

import com.psychometric.platform.features.itembank.derailers.entity.DerailerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DerailerTypeRepository extends JpaRepository<DerailerType, Long> {
    Optional<DerailerType> findByNameAr(String nameAr);
}
