package com.psychometric.platform.features.assessment.service;

import com.psychometric.platform.features.assessment.domain.enums.BatteryType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemSamplingService {

    private final JdbcTemplate jdbcTemplate;

    public ItemSamplingService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Executes stratified random item sampling without replacement for the given battery.
     */
    public List<Long> sampleItemsForBattery(BatteryType batteryType) {
        return switch (batteryType) {
            case PQ10 -> samplePersonalityItems(140);
            case DERAILERS -> sampleDerailerItems(60);
            case SJT -> sampleSjtItems(16);
            case GCAT -> sampleGcatItems(42);
        };
    }

    /**
     * Samples 140 personality items balanced across the 8 core competencies (18 for first 4, 17 for next 4).
     */
    public List<Long> samplePersonalityItems(int targetCount) {
        Set<Long> sampledSet = new LinkedHashSet<>();
        // Constrained competencies first to guarantee sufficient items per stratum:
        // 2 (INITIATIVE: 25), 6 (SKILL_DEVELOPMENT: 28), 5 (STRATEGIC_THINKING: 37), 8 (PLANNING: 41),
        // 3 (DECISION_MAKING: 54), 7 (ADAPTABILITY: 54), 1 (COMMUNICATION: 55), 4 (LEADERSHIP: 66)
        int[] compOrder = {2, 6, 5, 8, 3, 7, 1, 4};

        for (int cId : compOrder) {
            int quota = (cId <= 4) ? 18 : 17;
            List<Long> ids = jdbcTemplate.queryForList(
                    "SELECT DISTINCT pic.item_id FROM personality_item_competencies pic " +
                    "JOIN personality_items pi ON pic.item_id = pi.id " +
                    "WHERE pic.competency_id = ? AND pi.is_active = true " +
                    "ORDER BY RAND()",
                    Long.class, cId
            );

            int added = 0;
            for (Long id : ids) {
                if (!sampledSet.contains(id)) {
                    sampledSet.add(id);
                    added++;
                    if (added >= quota) break;
                }
            }
        }

        if (sampledSet.size() < targetCount) {
            List<Long> remainder = jdbcTemplate.queryForList(
                    "SELECT id FROM personality_items WHERE is_active = true ORDER BY RAND()",
                    Long.class
            );
            for (Long id : remainder) {
                sampledSet.add(id);
                if (sampledSet.size() >= targetCount) break;
            }
        }

        List<Long> result = new ArrayList<>(sampledSet);
        if (result.size() > targetCount) {
            result = result.subList(0, targetCount);
        }
        Collections.shuffle(result);
        return result;
    }

    /**
     * Samples 60 derailer items balanced equally (10 each) across the 6 derailer categories.
     */
    public List<Long> sampleDerailerItems(int targetCount) {
        List<Long> allSampled = new ArrayList<>();
        // 6 Derailer types (IDs 1..6)
        for (int tId = 1; tId <= 6; tId++) {
            List<Long> ids = jdbcTemplate.queryForList(
                    "SELECT DISTINCT dit.item_id FROM derailer_item_types dit " +
                    "JOIN derailer_items di ON dit.item_id = di.id " +
                    "WHERE dit.type_id = ? AND di.is_active = true " +
                    "ORDER BY RAND() LIMIT 10",
                    Long.class, tId
            );
            allSampled.addAll(ids);
        }

        if (allSampled.size() < targetCount) {
            Set<Long> existing = new HashSet<>(allSampled);
            List<Long> remainder = jdbcTemplate.queryForList(
                    "SELECT id FROM derailer_items WHERE is_active = true ORDER BY RAND()",
                    Long.class
            );
            for (Long id : remainder) {
                if (existing.add(id)) {
                    allSampled.add(id);
                    if (allSampled.size() >= targetCount) break;
                }
            }
        }

        List<Long> result = new ArrayList<>(allSampled);
        if (result.size() > targetCount) {
            result = result.subList(0, targetCount);
        }
        Collections.shuffle(result);
        return result;
    }

    /**
     * Samples 16 SJT scenarios purely at random from the item bank.
     */
    public List<Long> sampleSjtItems(int targetCount) {
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM sjt_scenarios WHERE is_active = true ORDER BY RAND() LIMIT ?",
                Long.class, targetCount
        );

        if (ids.size() < targetCount) {
            List<Long> remainder = jdbcTemplate.queryForList(
                    "SELECT id FROM sjt_scenarios ORDER BY RAND() LIMIT ?",
                    Long.class, targetCount
            );
            ids = remainder;
        }

        List<Long> result = new ArrayList<>(ids);
        Collections.shuffle(result);
        return result;
    }

    /**
     * Samples 42 GCAT questions: 14 Abstract, 14 Numerical, 14 Verbal,
     * each with equal difficulty distribution (5 Easy, 5 Medium, 4 Hard).
     */
    public List<Long> sampleGcatItems(int targetCount) {
        List<Long> allSampled = new ArrayList<>();
        String[] subtests = {"ABSTRACT", "NUMERICAL", "VERBAL"};

        for (String sub : subtests) {
            // 5 Easy
            List<Long> easyIds = jdbcTemplate.queryForList(
                    "SELECT q.id FROM gcat_questions q " +
                    "JOIN gcat_subtests s ON q.subtest_id = s.id " +
                    "WHERE s.code = ? AND q.difficulty = 'EASY' AND q.is_active = true " +
                    "ORDER BY RAND() LIMIT 5",
                    Long.class, sub
            );
            allSampled.addAll(easyIds);

            // 5 Medium
            List<Long> medIds = jdbcTemplate.queryForList(
                    "SELECT q.id FROM gcat_questions q " +
                    "JOIN gcat_subtests s ON q.subtest_id = s.id " +
                    "WHERE s.code = ? AND q.difficulty = 'MEDIUM' AND q.is_active = true " +
                    "ORDER BY RAND() LIMIT 5",
                    Long.class, sub
            );
            allSampled.addAll(medIds);

            // 4 Hard
            List<Long> hardIds = jdbcTemplate.queryForList(
                    "SELECT q.id FROM gcat_questions q " +
                    "JOIN gcat_subtests s ON q.subtest_id = s.id " +
                    "WHERE s.code = ? AND q.difficulty = 'HARD' AND q.is_active = true " +
                    "ORDER BY RAND() LIMIT 4",
                    Long.class, sub
            );
            allSampled.addAll(hardIds);
        }

        if (allSampled.size() < targetCount) {
            Set<Long> existing = new HashSet<>(allSampled);
            List<Long> remainder = jdbcTemplate.queryForList(
                    "SELECT id FROM gcat_questions WHERE is_active = true ORDER BY RAND()",
                    Long.class
            );
            for (Long id : remainder) {
                if (existing.add(id)) {
                    allSampled.add(id);
                    if (allSampled.size() >= targetCount) break;
                }
            }
        }

        List<Long> result = new ArrayList<>(allSampled);
        if (result.size() > targetCount) {
            result = result.subList(0, targetCount);
        }
        Collections.shuffle(result);
        return result;
    }
}
