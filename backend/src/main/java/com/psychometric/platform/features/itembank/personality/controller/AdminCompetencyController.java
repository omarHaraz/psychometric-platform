package com.psychometric.platform.features.itembank.personality.controller;

import com.psychometric.platform.features.itembank.personality.dto.CompetencyAdminRequest;
import com.psychometric.platform.features.itembank.personality.dto.CompetencyAdminResponse;
import com.psychometric.platform.features.itembank.personality.service.AdminTaxonomyService;
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
@RequestMapping({"/api/admin/taxonomies/competencies", "/api/admin/items/competencies", "/api/admin/items/personality/taxonomies/competencies"})
@Tag(name = "Admin Competency Taxonomy API", description = "Endpoints for managing 8-Competency framework taxonomy")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminCompetencyController {

    private final AdminTaxonomyService taxonomyService;

    public AdminCompetencyController(AdminTaxonomyService taxonomyService) {
        this.taxonomyService = taxonomyService;
    }

    @GetMapping
    @Operation(summary = "Get all competencies with item counts")
    public ResponseEntity<List<CompetencyAdminResponse>> getAllCompetencies() {
        return ResponseEntity.ok(taxonomyService.getAllCompetencies());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new competency")
    public ResponseEntity<CompetencyAdminResponse> createCompetency(@Valid @RequestBody CompetencyAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taxonomyService.createCompetency(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existing competency")
    public ResponseEntity<CompetencyAdminResponse> updateCompetency(
            @PathVariable("id") Long id,
            @Valid @RequestBody CompetencyAdminRequest request
    ) {
        return ResponseEntity.ok(taxonomyService.updateCompetency(id, request));
    }
}
