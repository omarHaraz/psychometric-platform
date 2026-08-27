package com.psychometric.platform.features.itembank.gcat.controller;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.gcat.dto.GcatQuestionCandidateDto;
import com.psychometric.platform.features.itembank.gcat.dto.GcatSubtestResponse;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import com.psychometric.platform.features.itembank.gcat.service.GcatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items/gcat")
@Tag(name = "Candidate GCAT Items API", description = "Endpoints for fetching sanitized General Cognitive Ability Test items")
public class GcatController {

    private final GcatService gcatService;

    public GcatController(GcatService gcatService) {
        this.gcatService = gcatService;
    }

    @GetMapping("/subtests")
    @Operation(summary = "Get all GCAT subtests metadata and quotas")
    public ResponseEntity<List<GcatSubtestResponse>> getAllSubtests() {
        return ResponseEntity.ok(gcatService.getAllSubtests());
    }

    @GetMapping("/questions")
    @Operation(summary = "Get candidate sanitized GCAT questions by subtest and exam mode")
    public ResponseEntity<List<GcatQuestionCandidateDto>> getCandidateQuestions(
            @RequestParam(name = "subtest", required = false) GcatSubtestCode subtest,
            @RequestParam(name = "mode", required = false, defaultValue = "FULL") ExamMode mode
    ) {
        return ResponseEntity.ok(gcatService.getCandidateQuestions(subtest, mode));
    }

    @GetMapping("/questions/{id}")
    @Operation(summary = "Get single candidate sanitized GCAT question by ID")
    public ResponseEntity<GcatQuestionCandidateDto> getQuestionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(gcatService.getQuestionById(id));
    }
}
