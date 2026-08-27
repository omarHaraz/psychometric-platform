package com.psychometric.platform.features.itembank.sjt.controller;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.sjt.dto.SjtDomainResponse;
import com.psychometric.platform.features.itembank.sjt.dto.SjtScenarioResponse;
import com.psychometric.platform.features.itembank.sjt.service.SjtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items/sjt")
@Tag(name = "Candidate SJT Scenarios API", description = "Endpoints for fetching sanitized Situational Judgement Test scenarios")
public class SjtController {

    private final SjtService sjtService;

    public SjtController(SjtService sjtService) {
        this.sjtService = sjtService;
    }

    @GetMapping("/domains")
    @Operation(summary = "Get all SJT domains taxonomy")
    public ResponseEntity<List<SjtDomainResponse>> getAllDomains() {
        return ResponseEntity.ok(sjtService.getAllDomains());
    }

    @GetMapping("/scenarios")
    @Operation(summary = "Get candidate sanitized SJT scenarios by domain and exam mode")
    public ResponseEntity<List<SjtScenarioResponse>> getCandidateScenarios(
            @RequestParam(name = "domainId", required = false) Long domainId,
            @RequestParam(name = "mode", required = false, defaultValue = "FULL") ExamMode mode
    ) {
        return ResponseEntity.ok(sjtService.getCandidateScenarios(domainId, mode));
    }

    @GetMapping("/scenarios/{id}")
    @Operation(summary = "Get single candidate sanitized SJT scenario by ID")
    public ResponseEntity<SjtScenarioResponse> getScenarioById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sjtService.getScenarioById(id));
    }
}
