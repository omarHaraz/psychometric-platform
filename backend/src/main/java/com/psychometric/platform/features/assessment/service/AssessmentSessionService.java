package com.psychometric.platform.features.assessment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.features.assessment.domain.enums.AttemptState;
import com.psychometric.platform.features.assessment.domain.enums.BatteryType;
import com.psychometric.platform.features.assessment.domain.enums.SessionState;
import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import com.psychometric.platform.features.assessment.domain.model.AssessmentScore;
import com.psychometric.platform.features.assessment.domain.model.BatterySession;
import com.psychometric.platform.features.assessment.domain.model.CandidateResponse;
import com.psychometric.platform.features.assessment.dto.HeartbeatRequest;
import com.psychometric.platform.features.assessment.repository.AssessmentAttemptRepository;
import com.psychometric.platform.features.assessment.repository.AssessmentScoreRepository;
import com.psychometric.platform.features.assessment.repository.BatterySessionRepository;
import com.psychometric.platform.features.assessment.repository.CandidateResponseRepository;
import com.psychometric.platform.features.user.entity.User;
import com.psychometric.platform.features.user.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AssessmentSessionService {
    private final AssessmentAttemptRepository attemptRepo;
    private final BatterySessionRepository sessionRepo;
    private final CandidateResponseRepository responseRepo;
    private final UserRepository userRepo;
    private final RedisTemplate<String, String> redisTemplate;
    private final ItemSamplingService samplingService;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final AssessmentScoringService scoringService;
    private final AssessmentScoreRepository assessmentScoreRepo;

    public AssessmentSessionService(AssessmentAttemptRepository attemptRepo,
                                    BatterySessionRepository sessionRepo,
                                    CandidateResponseRepository responseRepo,
                                    UserRepository userRepo,
                                    RedisTemplate<String, String> redisTemplate,
                                    ItemSamplingService samplingService,
                                    JdbcTemplate jdbcTemplate,
                                    AssessmentScoringService scoringService,
                                    AssessmentScoreRepository assessmentScoreRepo) {
        this.attemptRepo = attemptRepo;
        this.sessionRepo = sessionRepo;
        this.responseRepo = responseRepo;
        this.userRepo = userRepo;
        this.redisTemplate = redisTemplate;
        this.samplingService = samplingService;
        this.jdbcTemplate = jdbcTemplate;
        this.scoringService = scoringService;
        this.assessmentScoreRepo = assessmentScoreRepo;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    private User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional
    public AssessmentAttempt assignAttempt(Long candidateId, String adminEmail) {
        User candidate = userRepo.findById(candidateId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));
        User admin = getUserByEmail(adminEmail);

        if (!candidate.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate account is disabled");
        }

        if (attemptRepo.existsByCandidateIdAndStateIn((long) candidate.getId(), 
                java.util.List.of(com.psychometric.platform.features.assessment.domain.enums.AttemptState.INIT, com.psychometric.platform.features.assessment.domain.enums.AttemptState.IN_PROGRESS))) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "Candidate already has an active attempt");
        }

        AssessmentAttempt attempt = new AssessmentAttempt();
        attempt.setAttemptToken(UUID.randomUUID().toString());
        attempt.setCandidate(candidate);
        attempt.setCreatedBy(admin);
        attempt.setState(AttemptState.INIT);
        attempt.setCurrentBatteryIndex(0);
        attempt.setCreatedAt(Instant.now());

        attempt = attemptRepo.save(attempt);

        // Create the 4 batteries sequentially
        // 0: PQ10 (40 mins), 1: SJT (45 mins), 2: DERAILERS (20 mins), 3: GCAT (20 mins)
        createBattery(attempt, 0, BatteryType.PQ10, 2400);
        createBattery(attempt, 1, BatteryType.SJT, 2700);
        createBattery(attempt, 2, BatteryType.DERAILERS, 1200);
        createBattery(attempt, 3, BatteryType.GCAT, 1200);

        return attemptRepo.save(attempt);
    }

    private void createBattery(AssessmentAttempt attempt, int order, BatteryType type, int timeLimit) {
        BatterySession s = new BatterySession();
        s.setAttempt(attempt);
        s.setSequenceOrder(order);
        s.setBatteryType(type);
        s.setState(SessionState.LOCKED);
        s.setTimeLimitSeconds(timeLimit);
        attempt.getBatterySessions().add(s);
    }

    public AssessmentAttempt getAttemptByToken(String token, String username) {
        User authenticatedUser = getUserByEmail(username);
        AssessmentAttempt attempt = attemptRepo.findByAttemptToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt not found"));
        if (attempt.getCandidate().getId() != authenticatedUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to access this attempt");
        }
        return attempt;
    }
    
    public AssessmentAttempt getPendingAttempt(String username) {
        User authenticatedUser = getUserByEmail(username);
        List<AssessmentAttempt> attempts = attemptRepo.findByCandidateIdOrderByCreatedAtDesc((long) authenticatedUser.getId());
        return attempts.stream()
                .filter(a -> a.getState() != AttemptState.SCORED)
                .findFirst()
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<AssessmentAttempt> getHistory(String username) {
        User authenticatedUser = getUserByEmail(username);
        return attemptRepo.findByCandidateIdOrderByCreatedAtDesc((long) authenticatedUser.getId());
    }
    @Transactional
    public AssessmentAttempt startAttempt(String token, String username) {
        AssessmentAttempt attempt = getAttemptByToken(token, username);
        if (attempt.getState() != AttemptState.INIT) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attempt is already started or scored");
        }

        attempt.setState(AttemptState.IN_PROGRESS);
        attempt.setStartTime(Instant.now());

        BatterySession firstSession = attempt.getBatterySessions().stream()
                .filter(s -> s.getSequenceOrder() == 0)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("First battery not found"));

        firstSession.setState(SessionState.IN_PROGRESS);
        firstSession.setStartTime(Instant.now());

        // Perform stratified item sampling for battery 0 (PQ10)
        List<Long> sampledIds = samplingService.sampleItemsForBattery(firstSession.getBatteryType());
        firstSession.setSampledItemIds(sampledIds);

        // Set Redis Timer
        String timerKey = "battery_session:" + firstSession.getId() + ":timer";
        redisTemplate.opsForValue().set(timerKey, String.valueOf(firstSession.getStartTime().toEpochMilli()));

        return attemptRepo.save(attempt);
    }

    @Transactional
    public long handleHeartbeat(Long sessionId, HeartbeatRequest request, String username) {
        BatterySession session = validateSessionAction(sessionId, username);
        
        long remaining = getRemainingTimeSeconds(session);
        if (remaining <= 0) {
            autoSubmitSession(session);
            return 0;
        }

        // Buffer responses in Redis
        try {
            String respKey = "battery_session:" + sessionId + ":responses";
            String json = objectMapper.writeValueAsString(request.getResponses());
            redisTemplate.opsForValue().set(respKey, json, remaining + 60, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize responses", e);
        }

        return remaining;
    }

    @Transactional
    public AssessmentAttempt submitSession(Long sessionId, String username) {
        return submitSession(sessionId, null, username);
    }

    @Transactional
    public AssessmentAttempt submitSession(Long sessionId, HeartbeatRequest request, String username) {
        BatterySession session = validateSessionAction(sessionId, username);

        // If direct responses were passed in the submit payload, persist them immediately
        if (request != null && request.getResponses() != null && !request.getResponses().isEmpty()) {
            try {
                String respKey = "battery_session:" + sessionId + ":responses";
                String json = objectMapper.writeValueAsString(request.getResponses());
                redisTemplate.opsForValue().set(respKey, json, 300, TimeUnit.SECONDS);
            } catch (Exception e) {
                // fall back to saving directly
                for (HeartbeatRequest.ResponseDto dto : request.getResponses()) {
                    CandidateResponse cr = new CandidateResponse();
                    cr.setBatterySession(session);
                    cr.setItemId(dto.getItemId());
                    cr.setSelectedLikert(dto.getSelectedLikert());
                    cr.setRankingOrder(dto.getRankingOrder());
                    cr.setSelectedOption(dto.getSelectedOption());
                    cr.setResponseTimeMs(dto.getResponseTimeMs());
                    cr.setSubmittedAt(Instant.now());
                    responseRepo.save(cr);
                }
            }
        }

        long remaining = getRemainingTimeSeconds(session);
        if (remaining <= 0) {
            return autoSubmitSession(session);
        }
        return processSubmit(session, SessionState.SUBMITTED);
    }

    private AssessmentAttempt processSubmit(BatterySession session, SessionState terminalState) {
        session.setState(terminalState);
        session.setSubmitTime(Instant.now());

        flushRedisBufferToDb(session);

        AssessmentAttempt attempt = session.getAttempt();
        int currentIndex = attempt.getCurrentBatteryIndex();
        
        if (currentIndex < 3) {
            // Unlock next battery
            attempt.setCurrentBatteryIndex(currentIndex + 1);
            BatterySession nextSession = attempt.getBatterySessions().stream()
                    .filter(s -> s.getSequenceOrder() == currentIndex + 1)
                    .findFirst()
                    .orElseThrow();
            
            nextSession.setState(SessionState.LOCKED);

            // Perform stratified item sampling for the newly unlocked battery
            List<Long> sampledIds = samplingService.sampleItemsForBattery(nextSession.getBatteryType());
            nextSession.setSampledItemIds(sampledIds);
        } else {
            // All done
            attempt.setState(AttemptState.ALL_SUBMITTED);
            attempt.setSubmitTime(Instant.now());
            attempt = attemptRepo.save(attempt);

            // Execute scoring pipeline
            try {
                scoringService.scoreAttempt(attempt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return attempt;
        }

        return attemptRepo.save(attempt);
    }

    @Transactional(readOnly = true)
    public AssessmentScore getAssessmentScoreByToken(String token, String username) {
        AssessmentAttempt attempt = getAttemptByToken(token, username);
        return assessmentScoreRepo.findByAttemptId(attempt.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Score report not generated yet"));
    }

    @Transactional(readOnly = true)
    public AssessmentScore getAssessmentScoreForAdmin(String token) {
        return assessmentScoreRepo.findByAttemptAttemptToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Score report not found for attempt"));
    }

    @Transactional
    public AssessmentAttempt autoSubmitSession(BatterySession session) {
        return processSubmit(session, SessionState.TIMED_OUT);
    }

    private void flushRedisBufferToDb(BatterySession session) {
        String respKey = "battery_session:" + session.getId() + ":responses";
        String json = redisTemplate.opsForValue().get(respKey);
        if (json != null) {
            try {
                List<HeartbeatRequest.ResponseDto> dtos = objectMapper.readValue(json, new TypeReference<>() {});
                for (HeartbeatRequest.ResponseDto dto : dtos) {
                    CandidateResponse cr = new CandidateResponse();
                    cr.setBatterySession(session);
                    cr.setItemId(dto.getItemId());
                    cr.setSelectedLikert(dto.getSelectedLikert());
                    cr.setRankingOrder(dto.getRankingOrder());
                    cr.setSelectedOption(dto.getSelectedOption());
                    cr.setResponseTimeMs(dto.getResponseTimeMs());
                    cr.setSubmittedAt(Instant.now());
                    responseRepo.save(cr);
                }
                redisTemplate.delete(respKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private BatterySession validateSessionAction(Long sessionId, String username) {
        return validateSessionAction(sessionId, username, false);
    }

    private BatterySession validateSessionAction(Long sessionId, String username, boolean allowLocked) {
        User authenticatedUser = getUserByEmail(username);
        BatterySession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        
        AssessmentAttempt attempt = session.getAttempt();
        if (attempt.getCandidate().getId() != authenticatedUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized");
        }
        
        if (!session.getSequenceOrder().equals(attempt.getCurrentBatteryIndex())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot interact with battery out of sequence");
        }
        
        if (session.getState() != SessionState.IN_PROGRESS && !(allowLocked && session.getState() == SessionState.LOCKED)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Session is not IN_PROGRESS");
        }
        
        return session;
    }

    private long getRemainingTimeSeconds(BatterySession session) {
        String timerKey = "battery_session:" + session.getId() + ":timer";
        String startTimeStr = redisTemplate.opsForValue().get(timerKey);
        if (startTimeStr == null) {
            if (session.getStartTime() == null) { return session.getTimeLimitSeconds(); }
            startTimeStr = String.valueOf(session.getStartTime().toEpochMilli());
        }
        
        long startMs = Long.parseLong(startTimeStr);
        long elapsedMs = System.currentTimeMillis() - startMs;
        long limitMs = session.getTimeLimitSeconds() * 1000L;
        long remainingSec = (limitMs - elapsedMs) / 1000;
        return Math.max(0, remainingSec);
    }

    /**
     * Resolves and returns the sanitized items in the EXACT stored order of sampledItemIds.
     */
    @Transactional
    public List<Map<String, Object>> getBatteryItems(Long sessionId, String username) {
        BatterySession session = validateSessionAction(sessionId, username, true);
        
        if (session.getState() == SessionState.LOCKED) {
            session.setState(SessionState.IN_PROGRESS);
            session.setStartTime(Instant.now());
            sessionRepo.save(session);
            
            String timerKey = "battery_session:" + session.getId() + ":timer";
            redisTemplate.opsForValue().set(timerKey, String.valueOf(session.getStartTime().toEpochMilli()));
        }

        List<Long> sampledIds = session.getSampledItemIds();
        if (sampledIds == null || sampledIds.isEmpty()) {
            sampledIds = samplingService.sampleItemsForBattery(session.getBatteryType());
            session.setSampledItemIds(sampledIds);
            sessionRepo.save(session);
        }

        return switch (session.getBatteryType()) {
            case PQ10 -> fetchSanitizedPersonalityItems(sampledIds);
            case DERAILERS -> fetchSanitizedDerailerItems(sampledIds);
            case SJT -> fetchSanitizedSjtItems(sampledIds);
            case GCAT -> fetchSanitizedGcatItems(sampledIds);
        };
    }

    private List<Map<String, Object>> fetchSanitizedPersonalityItems(List<Long> ids) {
        Map<Long, Map<String, Object>> itemMap = new HashMap<>();
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        jdbcTemplate.query(
                "SELECT id, statement_ar FROM personality_items WHERE id IN (" + inSql + ")",
                rs -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs.getLong("id"));
                    map.put("statementAr", rs.getString("statement_ar"));
                    itemMap.put(rs.getLong("id"), map);
                },
                ids.toArray()
        );

        List<Map<String, Object>> ordered = new ArrayList<>();
        for (Long id : ids) {
            if (itemMap.containsKey(id)) ordered.add(itemMap.get(id));
        }
        return ordered;
    }

    private List<Map<String, Object>> fetchSanitizedDerailerItems(List<Long> ids) {
        Map<Long, Map<String, Object>> itemMap = new HashMap<>();
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        jdbcTemplate.query(
                "SELECT id, statement_ar, response_scale_type FROM derailer_items WHERE id IN (" + inSql + ")",
                rs -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs.getLong("id"));
                    map.put("statementAr", rs.getString("statement_ar"));
                    map.put("responseScaleType", rs.getString("response_scale_type"));
                    itemMap.put(rs.getLong("id"), map);
                },
                ids.toArray()
        );

        List<Map<String, Object>> ordered = new ArrayList<>();
        for (Long id : ids) {
            if (itemMap.containsKey(id)) ordered.add(itemMap.get(id));
        }
        return ordered;
    }

    private List<Map<String, Object>> fetchSanitizedSjtItems(List<Long> ids) {
        Map<Long, Map<String, Object>> itemMap = new HashMap<>();
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        jdbcTemplate.query(
                "SELECT id, item_code, title_ar, narrative_ar, scenario_image_url FROM sjt_scenarios WHERE id IN (" + inSql + ")",
                rs -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs.getLong("id"));
                    map.put("itemCode", rs.getString("item_code"));
                    map.put("titleAr", rs.getString("title_ar"));
                    map.put("narrativeAr", rs.getString("narrative_ar"));
                    map.put("scenarioImageUrl", rs.getString("scenario_image_url"));
                    map.put("options", new ArrayList<Map<String, Object>>());
                    itemMap.put(rs.getLong("id"), map);
                },
                ids.toArray()
        );

        // Fetch options without scoring key / expert rank
        jdbcTemplate.query(
                "SELECT id, scenario_id, option_key, action_text_ar FROM sjt_options WHERE scenario_id IN (" + inSql + ") ORDER BY option_key ASC",
                rs -> {
                    Long scenarioId = rs.getLong("scenario_id");
                    if (itemMap.containsKey(scenarioId)) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> opts = (List<Map<String, Object>>) itemMap.get(scenarioId).get("options");
                        Map<String, Object> opt = new HashMap<>();
                        opt.put("id", rs.getLong("id"));
                        opt.put("optionKey", rs.getString("option_key"));
                        opt.put("statementAr", rs.getString("action_text_ar"));
                        opts.add(opt);
                    }
                },
                ids.toArray()
        );

        List<Map<String, Object>> ordered = new ArrayList<>();
        for (Long id : ids) {
            if (itemMap.containsKey(id)) ordered.add(itemMap.get(id));
        }
        return ordered;
    }

    private List<Map<String, Object>> fetchSanitizedGcatItems(List<Long> ids) {
        Map<Long, Map<String, Object>> itemMap = new HashMap<>();
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        jdbcTemplate.query(
                "SELECT id, item_code, title_ar, prompt_text_ar, pattern_type_ar, question_image_url FROM gcat_questions WHERE id IN (" + inSql + ")",
                rs -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs.getLong("id"));
                    map.put("itemCode", rs.getString("item_code"));
                    map.put("titleAr", rs.getString("title_ar"));
                    map.put("promptTextAr", rs.getString("prompt_text_ar"));
                    map.put("patternTypeAr", rs.getString("pattern_type_ar"));
                    map.put("questionImageUrl", rs.getString("question_image_url"));
                    map.put("options", new ArrayList<Map<String, Object>>());
                    itemMap.put(rs.getLong("id"), map);
                },
                ids.toArray()
        );

        // Fetch options without correct key
        jdbcTemplate.query(
                "SELECT id, question_id, option_key, option_text_ar, option_image_url FROM gcat_options WHERE question_id IN (" + inSql + ") ORDER BY option_key ASC",
                rs -> {
                    Long questionId = rs.getLong("question_id");
                    if (itemMap.containsKey(questionId)) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> opts = (List<Map<String, Object>>) itemMap.get(questionId).get("options");
                        Map<String, Object> opt = new HashMap<>();
                        opt.put("id", rs.getLong("id"));
                        opt.put("optionKey", rs.getString("option_key"));
                        opt.put("textAr", rs.getString("option_text_ar"));
                        opt.put("imageUrl", rs.getString("option_image_url"));
                        opts.add(opt);
                    }
                },
                ids.toArray()
        );

        List<Map<String, Object>> ordered = new ArrayList<>();
        for (Long id : ids) {
            if (itemMap.containsKey(id)) ordered.add(itemMap.get(id));
        }
        return ordered;
    }
}
