package com.psychometric.platform.features.assessment.controller;

import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import com.psychometric.platform.features.assessment.dto.HeartbeatRequest;
import com.psychometric.platform.features.assessment.service.AssessmentSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attempts")
@PreAuthorize("hasRole('CANDIDATE')")
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class CandidateAssessmentController {

    private final AssessmentSessionService sessionService;

    public CandidateAssessmentController(AssessmentSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/me/pending")
    public ResponseEntity<AssessmentAttempt> getPendingAttempt(@AuthenticationPrincipal String username) {
        AssessmentAttempt attempt = sessionService.getPendingAttempt(username);
        if (attempt == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attempt);
    }

    @GetMapping("/{token}")
    public ResponseEntity<AssessmentAttempt> getAttempt(@PathVariable String token,
                                                        @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(sessionService.getAttemptByToken(token, username));
    }

    @PostMapping("/{token}/start")
    public ResponseEntity<AssessmentAttempt> startAttempt(@PathVariable String token,
                                                          @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(sessionService.startAttempt(token, username));
    }

    @GetMapping("/battery-sessions/{id}/items")
    public ResponseEntity<List<Map<String, Object>>> getBatteryItems(@PathVariable Long id,
                                                                    @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(sessionService.getBatteryItems(id, username));
    }

    @PostMapping("/battery-sessions/{id}/heartbeat")
    public ResponseEntity<Map<String, Long>> heartbeat(@PathVariable Long id,
                                                       @RequestBody HeartbeatRequest request,
                                                       @AuthenticationPrincipal String username) {
        long remaining = sessionService.handleHeartbeat(id, request, username);
        return ResponseEntity.ok(Map.of("remainingTimeSeconds", remaining));
    }

    @PostMapping("/battery-sessions/{id}/submit")
    public ResponseEntity<AssessmentAttempt> submitSession(@PathVariable Long id,
                                                           @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(sessionService.submitSession(id, username));
    }
    
    @GetMapping("/{token}/report")
    public ResponseEntity<?> getReport(@PathVariable String token,
                                       @AuthenticationPrincipal String username) {
        AssessmentAttempt attempt = sessionService.getAttemptByToken(token, username);
        if (attempt.getState() != com.psychometric.platform.features.assessment.domain.enums.AttemptState.SCORED) {
            return ResponseEntity.status(404).body("Report not ready");
        }
        return ResponseEntity.ok("Report Stub");
    }
}
