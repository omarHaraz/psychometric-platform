package com.psychometric.platform.features.itembank.gcat.controller;

import com.psychometric.platform.features.itembank.gcat.dto.GcatQuestionAdminRequest;
import com.psychometric.platform.features.itembank.gcat.dto.GcatQuestionAdminResponse;
import com.psychometric.platform.features.itembank.gcat.dto.GcatSubtestAdminRequest;
import com.psychometric.platform.features.itembank.gcat.dto.GcatSubtestAdminResponse;
import com.psychometric.platform.features.itembank.gcat.service.AdminGcatItemService;
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
@RequestMapping("/api/admin/items/cognitive")
@Tag(name = "Admin Cognitive (GCAT) Item Bank API", description = "Endpoints for managing GCAT questions, options, and subtest dimensions")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminGcatItemController {

    private final AdminGcatItemService gcatItemService;

    public AdminGcatItemController(AdminGcatItemService gcatItemService) {
        this.gcatItemService = gcatItemService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new GCAT cognitive question")
    public ResponseEntity<GcatQuestionAdminResponse> create(@Valid @RequestBody GcatQuestionAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gcatItemService.create(request));
    }

    @GetMapping
    @Operation(summary = "Get all GCAT questions for admin")
    public ResponseEntity<List<GcatQuestionAdminResponse>> getAll() {
        return ResponseEntity.ok(gcatItemService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get GCAT question by ID for admin")
    public ResponseEntity<GcatQuestionAdminResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(gcatItemService.getById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update GCAT question")
    public ResponseEntity<GcatQuestionAdminResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody GcatQuestionAdminRequest request
    ) {
        return ResponseEntity.ok(gcatItemService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete GCAT question")
    public ResponseEntity<Void> softDelete(@PathVariable("id") Long id) {
        gcatItemService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @Operation(summary = "Enable GCAT question")
    public ResponseEntity<GcatQuestionAdminResponse> enable(@PathVariable("id") Long id) {
        return ResponseEntity.ok(gcatItemService.enable(id));
    }

    @PatchMapping("/{id}/disable")
    @Operation(summary = "Disable GCAT question")
    public ResponseEntity<GcatQuestionAdminResponse> disable(@PathVariable("id") Long id) {
        return ResponseEntity.ok(gcatItemService.disable(id));
    }

    @PatchMapping("/{id}/reactivate")
    @Operation(summary = "Reactivate GCAT question")
    public ResponseEntity<GcatQuestionAdminResponse> reactivate(@PathVariable("id") Long id) {
        return ResponseEntity.ok(gcatItemService.reactivate(id));
    }

    // Subtest Taxonomy endpoints
    @GetMapping({"/taxonomies/subtests", "/subtests"})
    @Operation(summary = "Get all subtests taxonomy with question counts")
    public ResponseEntity<List<GcatSubtestAdminResponse>> getAllSubtests() {
        return ResponseEntity.ok(gcatItemService.getAllSubtests());
    }

    @PutMapping(value = {"/taxonomies/subtests/{id}", "/subtests/{id}"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update subtest quotas and configurations")
    public ResponseEntity<GcatSubtestAdminResponse> updateSubtest(
            @PathVariable("id") Long id,
            @Valid @RequestBody GcatSubtestAdminRequest request
    ) {
        return ResponseEntity.ok(gcatItemService.updateSubtest(id, request));
    }
}
