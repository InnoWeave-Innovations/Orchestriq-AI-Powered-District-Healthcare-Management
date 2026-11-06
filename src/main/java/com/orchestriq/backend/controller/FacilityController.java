package com.orchestriq.backend.controller;

import com.orchestriq.backend.dto.FacilityCreateRequest;
import com.orchestriq.backend.dto.FacilityResponse;
import com.orchestriq.backend.dto.FacilityStatusRequest;
import com.orchestriq.backend.dto.FacilityUpdateRequest;
import com.orchestriq.backend.repository.HealthcareFacilityRepository;
import com.orchestriq.backend.service.FacilityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/facilities")
public class FacilityController {
    private final FacilityService facilityService;
    private final HealthcareFacilityRepository healthcareFacilityRepository;
    public FacilityController(FacilityService facilityService,HealthcareFacilityRepository healthcareFacilityRepository){
        this.facilityService = facilityService;
        this.healthcareFacilityRepository = healthcareFacilityRepository;
    }
    @PostMapping
    @PreAuthorize("hasRole('DISTRICT_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public FacilityResponse createFacility(@Valid @RequestBody FacilityCreateRequest request){
        return facilityService.createFacility(request);
    }
    @GetMapping
    @PreAuthorize("hasRole('DISTRICT_ADMIN')")
    public List<FacilityResponse> getAllFacilities(){
        return facilityService.getAllFacility();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DISTRICT_ADMIN')")
    public FacilityResponse getFacilityById(@PathVariable UUID id){
        return facilityService.getFacilityById(id);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DISTRICT_ADMIN')")
    public FacilityResponse updateFacility(@PathVariable UUID id, @Valid @RequestBody FacilityUpdateRequest request){
        return facilityService.updateFacility(id,request);
    }
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('DISTRICT_ADMIN')")
    public FacilityResponse updateFacilityStatus(@PathVariable UUID id, @Valid @RequestBody FacilityStatusRequest request){
        return facilityService.updateFacilityStatus(id,request);
    }
}
