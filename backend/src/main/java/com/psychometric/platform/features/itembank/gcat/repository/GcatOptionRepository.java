package com.psychometric.platform.features.itembank.gcat.repository;

import com.psychometric.platform.features.itembank.gcat.entity.GcatOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GcatOptionRepository extends JpaRepository<GcatOption, Long> {
    List<GcatOption> findByQuestion_Id(Long questionId);
}
