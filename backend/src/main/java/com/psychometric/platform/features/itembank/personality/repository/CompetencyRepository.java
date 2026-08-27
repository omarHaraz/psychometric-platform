package com.psychometric.platform.features.itembank.personality.repository;

import com.psychometric.platform.features.itembank.personality.entity.Competency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetencyRepository extends JpaRepository<Competency, Long> {
    Optional<Competency> findByCode(String code);
    List<Competency> findAllByOrderByDisplayOrderAsc();
}
