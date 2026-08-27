package com.psychometric.platform.features.itembank.derailers.controller;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerItemResponse;
import com.psychometric.platform.features.itembank.derailers.dto.DerailerTypeResponse;
import com.psychometric.platform.features.itembank.derailers.service.DerailerItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/items/derailers")
@Tag(name = "Candidate Derailer Items API", description = "Endpoints for fetching sanitized derailer assessment items")
public class DerailerItemController {

    private final DerailerItemService derailerItemService;

    public DerailerItemController(DerailerItemService derailerItemService) {
        this.derailerItemService = derailerItemService;
    }

    @GetMapping
    @Operation(summary = "Get sanitized derailer items by exam mode")
    public ResponseEntity<List<DerailerItemResponse>> getDerailerItems(
            @RequestParam(name = "mode", required = false, defaultValue = "FULL") ExamMode mode
    ) {
        return ResponseEntity.ok(derailerItemService.getDerailerItems(mode));
    }

    @GetMapping("/types")
    @Operation(summary = "Get all derailer types with indicators")
    public ResponseEntity<List<DerailerTypeResponse>> getDerailerTypes() {
        return ResponseEntity.ok(derailerItemService.getDerailerTypes());
    }
}
