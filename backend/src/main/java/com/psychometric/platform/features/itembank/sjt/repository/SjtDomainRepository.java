package com.psychometric.platform.features.itembank.sjt.repository;

import com.psychometric.platform.features.itembank.sjt.entity.SjtDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SjtDomainRepository extends JpaRepository<SjtDomain, Long> {
    Optional<SjtDomain> findByCode(String code);
    List<SjtDomain> findAllByOrderByDisplayOrderAsc();
}
