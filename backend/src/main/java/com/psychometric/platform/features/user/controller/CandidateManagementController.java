package com.psychometric.platform.features.user.controller;
import com.psychometric.platform.features.user.entity.User;


import com.psychometric.platform.features.user.dto.response.CandidateResponse;
import com.psychometric.platform.features.user.dto.request.CandidateUpdateRequest;
import com.psychometric.platform.features.user.dto.request.CandidateCreateRequest;
import org.springframework.http.HttpStatus;
import com.psychometric.platform.features.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate/management")
@PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
public class CandidateManagementController
{

    @Autowired
    private UserService userService;

    @GetMapping
    
    @PostMapping
    public ResponseEntity<CandidateResponse> createCandidate(
            @Valid @RequestBody CandidateCreateRequest request) {
        CandidateResponse response = userService.createCandidateUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CandidateResponse>> getCustomers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CandidateUpdateRequest request) {



        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateCustomer(@PathVariable Long id) {

        userService.reactivateUser(id);

        return ResponseEntity.noContent().build();
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateCustomer(@PathVariable Long id) {

        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }





}
