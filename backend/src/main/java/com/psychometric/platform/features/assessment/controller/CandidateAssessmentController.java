package com.psychometric.platform.features.assessment.controller;

import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import com.psychometric.platform.features.assessment.domain.model.AssessmentScore;
import com.psychometric.platform.features.assessment.dto.HeartbeatRequest;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto;
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
public class CandidateAssessmentController {

    private final AssessmentSessionService sessionService;

    public CandidateAssessmentController(AssessmentSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/me/pending")
    public ResponseEntity<AssessmentAttempt> getPendingAttempt(@AuthenticationPrincipal String username) {
        AssessmentAttempt attempt = sessionService.getPendingAttempt(username);
        if (attempt == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(attempt);
    }

    @GetMapping("/me/history")
    public ResponseEntity<List<AssessmentAttempt>> getHistory(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok(sessionService.getHistory(username));
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
                                                           @RequestBody(required = false) HeartbeatRequest request,
                                                           @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(sessionService.submitSession(id, request, username));
    }

    @GetMapping("/{token}/score")
    public ResponseEntity<AssessmentScoreResponseDto> getScore(
            @PathVariable String token,
            @AuthenticationPrincipal String username) {
        AssessmentScore score = sessionService.getAssessmentScoreByToken(token, username);
        return ResponseEntity.ok(AssessmentScoreResponseDto.fromEntity(score));
    }

    @GetMapping("/{token}/report")
    public ResponseEntity<AssessmentScoreResponseDto> getReport(
            @PathVariable String token,
            @AuthenticationPrincipal String username) {
        AssessmentScore score = sessionService.getAssessmentScoreByToken(token, username);
        return ResponseEntity.ok(AssessmentScoreResponseDto.fromEntity(score));
    }
}
