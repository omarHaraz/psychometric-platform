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
     * Samples 140 personality items balanced equally across the 8 core competencies.
     */
    public List<Long> samplePersonalityItems(int targetCount) {
        Set<Long> selected = new LinkedHashSet<>();
        // 8 competencies: IDs 1..8. Draw 18 from first 4, 17 from next 4 -> 140 total
        for (int cId = 1; cId <= 8; cId++) {
            int quota = (cId <= 4) ? 18 : 17;
            List<Long> ids = jdbcTemplate.queryForList(
                    "SELECT DISTINCT pic.item_id FROM personality_item_competencies pic " +
                    "JOIN personality_items pi ON pic.item_id = pi.id " +
                    "WHERE pic.competency_id = ? AND pi.is_active = true " +
                    "ORDER BY RAND() LIMIT ?",
                    Long.class, cId, quota
            );
            selected.addAll(ids);
        }

        if (selected.size() < targetCount) {
            List<Long> remainder = jdbcTemplate.queryForList(
                    "SELECT id FROM personality_items WHERE is_active = true ORDER BY RAND()",
                    Long.class
            );
            for (Long id : remainder) {
                selected.add(id);
                if (selected.size() >= targetCount) break;
            }
        }

        List<Long> result = new ArrayList<>(selected);
        if (result.size() > targetCount) {
            result = result.subList(0, targetCount);
        }
        Collections.shuffle(result);
        return result;
    }

    /**
     * Samples 60 derailer items balanced equally (10 each) across the 6 derailer types.
     */
    public List<Long> sampleDerailerItems(int targetCount) {
        Set<Long> selected = new LinkedHashSet<>();
        // 6 Derailer types (IDs 1..6)
        for (int tId = 1; tId <= 6; tId++) {
            List<Long> ids = jdbcTemplate.queryForList(
                    "SELECT DISTINCT dit.item_id FROM derailer_item_types dit " +
                    "JOIN derailer_items di ON dit.item_id = di.id " +
                    "WHERE dit.type_id = ? AND di.is_active = true " +
                    "ORDER BY RAND() LIMIT 10",
                    Long.class, tId
            );
            selected.addAll(ids);
        }

        if (selected.size() < targetCount) {
            List<Long> remainder = jdbcTemplate.queryForList(
                    "SELECT id FROM derailer_items WHERE is_active = true ORDER BY RAND()",
                    Long.class
            );
            for (Long id : remainder) {
                selected.add(id);
                if (selected.size() >= targetCount) break;
            }
        }

        List<Long> result = new ArrayList<>(selected);
        if (result.size() > targetCount) {
            result = result.subList(0, targetCount);
        }
        Collections.shuffle(result);
        return result;
    }

    /**
     * Samples 16 SJT scenarios balanced across the 5 domains (3 each + 1 remainder).
     */
    public List<Long> sampleSjtItems(int targetCount) {
        Set<Long> selected = new LinkedHashSet<>();
        // 5 SJT domains (IDs 1..5)
        for (int dId = 1; dId <= 5; dId++) {
            List<Long> ids = jdbcTemplate.queryForList(
                    "SELECT id FROM sjt_scenarios " +
                    "WHERE domain_id = ? AND is_active = true " +
                    "ORDER BY RAND() LIMIT 3",
                    Long.class, dId
            );
            selected.addAll(ids);
        }

        if (selected.size() < targetCount) {
            List<Long> remainder = jdbcTemplate.queryForList(
                    "SELECT id FROM sjt_scenarios WHERE is_active = true ORDER BY RAND()",
                    Long.class
            );
            for (Long id : remainder) {
                selected.add(id);
                if (selected.size() >= targetCount) break;
            }
        }

        List<Long> result = new ArrayList<>(selected);
        if (result.size() > targetCount) {
            result = result.subList(0, targetCount);
        }
        Collections.shuffle(result);
        return result;
    }

    /**
     * Samples 42 GCAT questions: 14 Abstract, 14 Numerical, 14 Verbal,
     * each with equal difficulty distribution (5 Easy, 5 Medium, 4 Hard).
     */
    public List<Long> sampleGcatItems(int targetCount) {
        Set<Long> selected = new LinkedHashSet<>();
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
            selected.addAll(easyIds);

            // 5 Medium
            List<Long> medIds = jdbcTemplate.queryForList(
                    "SELECT q.id FROM gcat_questions q " +
                    "JOIN gcat_subtests s ON q.subtest_id = s.id " +
                    "WHERE s.code = ? AND q.difficulty = 'MEDIUM' AND q.is_active = true " +
                    "ORDER BY RAND() LIMIT 5",
                    Long.class, sub
            );
            selected.addAll(medIds);

            // 4 Hard
            List<Long> hardIds = jdbcTemplate.queryForList(
                    "SELECT q.id FROM gcat_questions q " +
                    "JOIN gcat_subtests s ON q.subtest_id = s.id " +
                    "WHERE s.code = ? AND q.difficulty = 'HARD' AND q.is_active = true " +
                    "ORDER BY RAND() LIMIT 4",
                    Long.class, sub
            );
            selected.addAll(hardIds);
        }

        if (selected.size() < targetCount) {
            List<Long> remainder = jdbcTemplate.queryForList(
                    "SELECT id FROM gcat_questions WHERE is_active = true ORDER BY RAND()",
                    Long.class
            );
            for (Long id : remainder) {
                selected.add(id);
                if (selected.size() >= targetCount) break;
            }
        }

        List<Long> result = new ArrayList<>(selected);
        if (result.size() > targetCount) {
            result = result.subList(0, targetCount);
        }
        Collections.shuffle(result);
        return result;
    }
}
