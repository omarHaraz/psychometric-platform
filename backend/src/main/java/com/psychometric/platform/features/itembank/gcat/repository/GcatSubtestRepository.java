package com.psychometric.platform.features.itembank.gcat.repository;

import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtest;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GcatSubtestRepository extends JpaRepository<GcatSubtest, Long> {
    Optional<GcatSubtest> findByCode(GcatSubtestCode code);
}
