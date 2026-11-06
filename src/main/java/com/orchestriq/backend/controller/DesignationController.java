
package com.orchestriq.backend.controller;

import com.orchestriq.backend.dto.DesignationRequest;
import com.orchestriq.backend.dto.DesignationResponse;
import com.orchestriq.backend.dto.DesignationStatusRequest;
import com.orchestriq.backend.service.DesignationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/designations")
public class DesignationController {

    private final DesignationService designationService;

    public DesignationController(
            DesignationService designationService) {
        this.designationService = designationService;
    }

    @PreAuthorize("hasRole('DISTRICT_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DesignationResponse createDesignation(
            @Valid @RequestBody DesignationRequest request) {

        return designationService.createDesignation(request);
    }

    @PreAuthorize("hasRole('DISTRICT_ADMIN')")
    @GetMapping
    public List<DesignationResponse> getAllDesignations() {
        return designationService.getAllDesignations();
    }

    @PreAuthorize("hasRole('DISTRICT_ADMIN')")
    @GetMapping("/{id}")
    public DesignationResponse getDesignationById(
            @PathVariable UUID id) {

        return designationService.getDesignationById(id);
    }

    @PreAuthorize("hasRole('DISTRICT_ADMIN')")
    @PutMapping("/{id}")
    public DesignationResponse updateDesignation(
            @PathVariable UUID id,
            @Valid @RequestBody DesignationRequest request) {

        return designationService.updateDesignation(id, request);
    }

    @PreAuthorize("hasRole('DISTRICT_ADMIN')")
    @PatchMapping("/{id}/status")
    public DesignationResponse changeDesignationStatus(
            @PathVariable UUID id,
            @Valid @RequestBody DesignationStatusRequest request) {

        return designationService.changeDesignationStatus(
                id, request.status());
    }
}
