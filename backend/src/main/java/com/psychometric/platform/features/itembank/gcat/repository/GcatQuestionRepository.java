package com.psychometric.platform.features.itembank.gcat.repository;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.gcat.entity.GcatQuestion;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface GcatQuestionRepository extends JpaRepository<GcatQuestion, Long> {

    Optional<GcatQuestion> findByItemCode(String itemCode);

    List<GcatQuestion> findBySubtest_CodeAndExamModeInAndActiveTrue(GcatSubtestCode subtestCode, Collection<ExamMode> examModes);

    List<GcatQuestion> findByExamModeInAndActiveTrue(Collection<ExamMode> examModes);

    List<GcatQuestion> findBySubtest_IdAndActiveTrue(Long subtestId);

    @Query("SELECT DISTINCT q FROM GcatQuestion q LEFT JOIN FETCH q.subtest LEFT JOIN FETCH q.options ORDER BY q.id ASC")
    List<GcatQuestion> findAllWithSubtestAndOptions();

    @Query("SELECT DISTINCT q FROM GcatQuestion q LEFT JOIN FETCH q.subtest LEFT JOIN FETCH q.options WHERE q.id = :id")
    Optional<GcatQuestion> findByIdWithOptions(@Param("id") Long id);

    @Query("SELECT DISTINCT q FROM GcatQuestion q LEFT JOIN FETCH q.subtest LEFT JOIN FETCH q.options WHERE q.subtest.code = :subtestCode AND q.examMode IN :modes AND q.active = true")
    List<GcatQuestion> findActiveBySubtestAndExamModeWithOptionCandidates(
            @Param("subtestCode") GcatSubtestCode subtestCode,
            @Param("modes") Collection<ExamMode> modes
    );

    @Query("SELECT DISTINCT q FROM GcatQuestion q LEFT JOIN FETCH q.subtest LEFT JOIN FETCH q.options WHERE q.examMode IN :modes AND q.active = true")
    List<GcatQuestion> findActiveByExamModeWithOptionCandidates(@Param("modes") Collection<ExamMode> modes);
}
