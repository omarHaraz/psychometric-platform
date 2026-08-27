package com.psychometric.platform.features.itembank.sjt.controller;

import com.psychometric.platform.features.itembank.sjt.dto.*;
import com.psychometric.platform.features.itembank.sjt.service.AdminSjtItemService;
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
@RequestMapping("/api/admin/items/sjt")
@Tag(name = "Admin Situational Judgement (SJT) Item Bank API", description = "Endpoints for managing SJT scenarios, options, and domain taxonomy")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminSjtItemController {

    private final AdminSjtItemService sjtItemService;

    public AdminSjtItemController(AdminSjtItemService sjtItemService) {
        this.sjtItemService = sjtItemService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new SJT scenario")
    public ResponseEntity<SjtScenarioAdminResponse> create(@Valid @RequestBody SjtScenarioAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sjtItemService.create(request));
    }

    @GetMapping
    @Operation(summary = "Get all SJT scenarios for admin")
    public ResponseEntity<List<SjtScenarioAdminResponse>> getAll() {
        return ResponseEntity.ok(sjtItemService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get SJT scenario by ID for admin")
    public ResponseEntity<SjtScenarioAdminResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sjtItemService.getById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update SJT scenario")
    public ResponseEntity<SjtScenarioAdminResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody SjtScenarioAdminRequest request
    ) {
        return ResponseEntity.ok(sjtItemService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete SJT scenario")
    public ResponseEntity<Void> softDelete(@PathVariable("id") Long id) {
        sjtItemService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @Operation(summary = "Enable SJT scenario")
    public ResponseEntity<SjtScenarioAdminResponse> enable(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sjtItemService.enable(id));
    }

    @PatchMapping("/{id}/disable")
    @Operation(summary = "Disable SJT scenario")
    public ResponseEntity<SjtScenarioAdminResponse> disable(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sjtItemService.disable(id));
    }

    @PatchMapping("/{id}/reactivate")
    @Operation(summary = "Reactivate SJT scenario")
    public ResponseEntity<SjtScenarioAdminResponse> reactivate(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sjtItemService.reactivate(id));
    }

    // Domain Taxonomy endpoints
    @GetMapping({"/taxonomies/domains", "/domains"})
    @Operation(summary = "Get all SJT domains taxonomy with scenario counts")
    public ResponseEntity<List<SjtDomainAdminResponse>> getAllDomains() {
        return ResponseEntity.ok(sjtItemService.getAllDomains());
    }

    @PostMapping(value = {"/taxonomies/domains", "/domains"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new SJT domain")
    public ResponseEntity<SjtDomainAdminResponse> createDomain(@Valid @RequestBody SjtDomainAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sjtItemService.createDomain(request));
    }

    @PutMapping(value = {"/taxonomies/domains/{id}", "/domains/{id}"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existing SJT domain")
    public ResponseEntity<SjtDomainAdminResponse> updateDomain(
            @PathVariable("id") Long id,
            @Valid @RequestBody SjtDomainAdminRequest request
    ) {
        return ResponseEntity.ok(sjtItemService.updateDomain(id, request));
    }
}
