package com.psychometric.platform.features.itembank.personality.controller;

import com.psychometric.platform.features.itembank.common.entity.ExamMode;
import com.psychometric.platform.features.itembank.personality.dto.PersonalityItemResponse;
import com.psychometric.platform.features.itembank.personality.service.PersonalityItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/items/personality")
@Tag(name = "Candidate Personality Items API", description = "Endpoints for fetching sanitized personality assessment items")
public class PersonalityItemController {

    private final PersonalityItemService personalityItemService;

    public PersonalityItemController(PersonalityItemService personalityItemService) {
        this.personalityItemService = personalityItemService;
    }

    @GetMapping
    @Operation(summary = "Get sanitized personality items by exam mode")
    public ResponseEntity<List<PersonalityItemResponse>> getPersonalityItems(
            @RequestParam(name = "mode", required = false, defaultValue = "QUICK") ExamMode mode
    ) {
        return ResponseEntity.ok(personalityItemService.getPersonalityItems(mode));
    }
}
