package com.psychometric.platform.features.assessment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.features.assessment.domain.enums.AttemptState;
import com.psychometric.platform.features.assessment.domain.enums.BatteryType;
import com.psychometric.platform.features.assessment.domain.enums.SessionState;
import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import com.psychometric.platform.features.assessment.domain.model.BatterySession;
import com.psychometric.platform.features.assessment.domain.model.CandidateResponse;
import com.psychometric.platform.features.assessment.dto.HeartbeatRequest;
import com.psychometric.platform.features.assessment.repository.AssessmentAttemptRepository;
import com.psychometric.platform.features.assessment.repository.BatterySessionRepository;
import com.psychometric.platform.features.assessment.repository.CandidateResponseRepository;
import com.psychometric.platform.features.user.entity.User;
import com.psychometric.platform.features.user.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AssessmentSessionService {
    private final AssessmentAttemptRepository attemptRepo;
    private final BatterySessionRepository sessionRepo;
    private final CandidateResponseRepository responseRepo;
    private final UserRepository userRepo;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public AssessmentSessionService(AssessmentAttemptRepository attemptRepo,
                                    BatterySessionRepository sessionRepo,
                                    CandidateResponseRepository responseRepo,
                                    UserRepository userRepo,
                                    RedisTemplate<String, String> redisTemplate) {
        this.attemptRepo = attemptRepo;
        this.sessionRepo = sessionRepo;
        this.responseRepo = responseRepo;
        this.userRepo = userRepo;
        this.redisTemplate = redisTemplate;
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

        if (attemptRepo.existsByCandidateIdAndStateNot((long) candidate.getId(), AttemptState.SCORED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Candidate already has an active attempt");
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

        // TODO: Trigger EmailService here to send attemptToken to candidate

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
        List<AssessmentAttempt> attempts = attemptRepo.findByCandidateId((long) authenticatedUser.getId());
        return attempts.stream()
                .filter(a -> a.getState() != AttemptState.SCORED)
                .findFirst()
                .orElse(null);
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
            redisTemplate.opsForValue().set(respKey, json, remaining + 60, TimeUnit.SECONDS); // Expire shortly after timer
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize responses", e);
        }

        return remaining;
    }

    @Transactional
    public AssessmentAttempt submitSession(Long sessionId, String username) {
        BatterySession session = validateSessionAction(sessionId, username);
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
            
            nextSession.setState(SessionState.IN_PROGRESS);
            nextSession.setStartTime(Instant.now());
            
            String timerKey = "battery_session:" + nextSession.getId() + ":timer";
            redisTemplate.opsForValue().set(timerKey, String.valueOf(nextSession.getStartTime().toEpochMilli()));
        } else {
            // All done
            attempt.setState(AttemptState.ALL_SUBMITTED);
            attempt.setSubmitTime(Instant.now());
            // TODO: Enqueue scoring job here
        }

        return attemptRepo.save(attempt);
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
                // Log failure, but do not block progression
                e.printStackTrace();
            }
        }
    }

    private BatterySession validateSessionAction(Long sessionId, String username) {
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
        
        if (session.getState() != SessionState.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Session is not IN_PROGRESS");
        }
        
        return session;
    }

    private long getRemainingTimeSeconds(BatterySession session) {
        String timerKey = "battery_session:" + session.getId() + ":timer";
        String startTimeStr = redisTemplate.opsForValue().get(timerKey);
        if (startTimeStr == null) {
            // fallback if redis key expired/lost
            if (session.getStartTime() == null) { return 0; }
            startTimeStr = String.valueOf(session.getStartTime().toEpochMilli());
        }
        
        long startMs = Long.parseLong(startTimeStr);
        long elapsedMs = System.currentTimeMillis() - startMs;
        long limitMs = session.getTimeLimitSeconds() * 1000L;
        long remainingSec = (limitMs - elapsedMs) / 1000;
        return Math.max(0, remainingSec);
    }
}
