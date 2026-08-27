package com.psychometric.platform.features.itembank.personality.controller;

import com.psychometric.platform.features.itembank.personality.dto.PersonalityItemAdminRequest;
import com.psychometric.platform.features.itembank.personality.dto.PersonalityItemAdminResponse;
import com.psychometric.platform.features.itembank.personality.service.AdminPersonalityItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/items/personality")
@Tag(name = "Admin Personality Item Bank API", description = "Endpoints for managing personality items")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminPersonalityItemController {

    private final AdminPersonalityItemService personalityItemService;

    public AdminPersonalityItemController(AdminPersonalityItemService personalityItemService) {
        this.personalityItemService = personalityItemService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new personality item")
    public ResponseEntity<PersonalityItemAdminResponse> create(@Valid @RequestBody PersonalityItemAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personalityItemService.create(request));
    }

    @GetMapping
    @Operation(summary = "Get all personality items for admin")
    public ResponseEntity<List<PersonalityItemAdminResponse>> getAll() {
        return ResponseEntity.ok(personalityItemService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get personality item by ID for admin")
    public ResponseEntity<PersonalityItemAdminResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(personalityItemService.getById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update personality item")
    public ResponseEntity<PersonalityItemAdminResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody PersonalityItemAdminRequest request
    ) {
        return ResponseEntity.ok(personalityItemService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete personality item")
    public ResponseEntity<Void> softDelete(@PathVariable("id") Long id) {
        personalityItemService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @Operation(summary = "Enable personality item")
    public ResponseEntity<PersonalityItemAdminResponse> enable(@PathVariable("id") Long id) {
        return ResponseEntity.ok(personalityItemService.enable(id));
    }

    @PatchMapping("/{id}/disable")
    @Operation(summary = "Disable personality item")
    public ResponseEntity<PersonalityItemAdminResponse> disable(@PathVariable("id") Long id) {
        return ResponseEntity.ok(personalityItemService.disable(id));
    }

    @PatchMapping("/{id}/reactivate")
    @Operation(summary = "Reactivate personality item")
    public ResponseEntity<PersonalityItemAdminResponse> reactivate(@PathVariable("id") Long id) {
        return ResponseEntity.ok(personalityItemService.reactivate(id));
    }
}
