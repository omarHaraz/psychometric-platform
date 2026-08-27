package com.psychometric.platform.features.itembank.derailers.controller;

import com.psychometric.platform.features.itembank.derailers.dto.*;
import com.psychometric.platform.features.itembank.derailers.service.AdminDerailerItemService;
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
@RequestMapping({"/api/admin/items/derailers", "/api/admin/items/derailer-types"})
@Tag(name = "Admin Derailer Item Bank API", description = "Endpoints for managing derailer items and risk taxonomy")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminDerailerItemController {

    private final AdminDerailerItemService derailerItemService;

    public AdminDerailerItemController(AdminDerailerItemService derailerItemService) {
        this.derailerItemService = derailerItemService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new derailer item")
    public ResponseEntity<DerailerItemAdminResponse> create(@Valid @RequestBody DerailerItemAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(derailerItemService.create(request));
    }

    @GetMapping
    @Operation(summary = "Get all derailer items for admin")
    public ResponseEntity<List<DerailerItemAdminResponse>> getAll() {
        return ResponseEntity.ok(derailerItemService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get derailer item by ID for admin")
    public ResponseEntity<DerailerItemAdminResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(derailerItemService.getById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update derailer item")
    public ResponseEntity<DerailerItemAdminResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody DerailerItemAdminRequest request
    ) {
        return ResponseEntity.ok(derailerItemService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete derailer item")
    public ResponseEntity<Void> softDelete(@PathVariable("id") Long id) {
        derailerItemService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @Operation(summary = "Enable derailer item")
    public ResponseEntity<DerailerItemAdminResponse> enable(@PathVariable("id") Long id) {
        return ResponseEntity.ok(derailerItemService.enable(id));
    }

    @PatchMapping("/{id}/disable")
    @Operation(summary = "Disable derailer item")
    public ResponseEntity<DerailerItemAdminResponse> disable(@PathVariable("id") Long id) {
        return ResponseEntity.ok(derailerItemService.disable(id));
    }

    @PatchMapping("/{id}/reactivate")
    @Operation(summary = "Reactivate derailer item")
    public ResponseEntity<DerailerItemAdminResponse> reactivate(@PathVariable("id") Long id) {
        return ResponseEntity.ok(derailerItemService.reactivate(id));
    }

    // Taxonomy Endpoints
    @GetMapping({"/taxonomies/types", "/types"})
    @Operation(summary = "Get all derailer types taxonomy with item counts")
    public ResponseEntity<List<DerailerTypeAdminResponse>> getAllTypes() {
        return ResponseEntity.ok(derailerItemService.getAllTypes());
    }

    @PostMapping(value = {"/taxonomies/types", "/types"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new derailer type")
    public ResponseEntity<DerailerTypeAdminResponse> createType(@Valid @RequestBody DerailerTypeAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(derailerItemService.createType(request));
    }

    @PutMapping(value = {"/taxonomies/types/{id}", "/types/{id}"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existing derailer type")
    public ResponseEntity<DerailerTypeAdminResponse> updateType(
            @PathVariable("id") Long id,
            @Valid @RequestBody DerailerTypeAdminRequest request
    ) {
        return ResponseEntity.ok(derailerItemService.updateType(id, request));
    }
}
