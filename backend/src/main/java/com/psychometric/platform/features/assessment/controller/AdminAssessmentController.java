package com.psychometric.platform.features.assessment.controller;

import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import com.psychometric.platform.features.assessment.dto.AdminAttemptCreateRequest;
import com.psychometric.platform.features.assessment.repository.AssessmentAttemptRepository;
import com.psychometric.platform.features.assessment.service.AssessmentSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/attempts")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminAssessmentController {

    private final AssessmentSessionService sessionService;
    private final AssessmentAttemptRepository attemptRepo;

    public AdminAssessmentController(AssessmentSessionService sessionService, AssessmentAttemptRepository attemptRepo) {
        this.sessionService = sessionService;
        this.attemptRepo = attemptRepo;
    }

    @PostMapping
    public ResponseEntity<AssessmentAttempt> createAttempt(@RequestBody AdminAttemptCreateRequest request,
                                                           @AuthenticationPrincipal String adminEmail) {
        AssessmentAttempt attempt = sessionService.assignAttempt(request.getCandidateId(), adminEmail);
        return ResponseEntity.ok(attempt);
    }

    @GetMapping
    public ResponseEntity<List<AssessmentAttempt>> getCandidateAttempts(@RequestParam Long candidateId) {
        return ResponseEntity.ok(attemptRepo.findByCandidateId(candidateId));
    }
}
