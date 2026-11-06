package com.orchestriq.backend.service;

import com.orchestriq.backend.dto.FacilityCreateRequest;
import com.orchestriq.backend.dto.FacilityResponse;
import com.orchestriq.backend.dto.FacilityStatusRequest;
import com.orchestriq.backend.dto.FacilityUpdateRequest;
import com.orchestriq.backend.entity.HealthcareFacility;
import com.orchestriq.backend.repository.HealthcareFacilityRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FacilityService {
    private final HealthcareFacilityRepository healthcareFacilityRepository;
    public FacilityService(HealthcareFacilityRepository healthcareFacilityRepository){
        this.healthcareFacilityRepository = healthcareFacilityRepository;
    }
    public FacilityResponse createFacility(FacilityCreateRequest request){
        if (healthcareFacilityRepository
                .existsByFacilityNameIgnoreCase(request.getFacilityName().trim())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Facility with this name already exists"
            );
        }
        HealthcareFacility facility = new HealthcareFacility();
        facility.setFacilityName(request.getFacilityName().trim());
        facility.setFacilityType(request.getFacilityType());
        facility.setAddress(request.getAddress());
        facility.setContactNumber(request.getContactNumber());
        facility.setStatus("ACTIVE");
        HealthcareFacility savedFacility =
                healthcareFacilityRepository.save(facility);
        return mapToResponse(savedFacility);
    }

    public List<FacilityResponse> getAllFacility(){
        List<HealthcareFacility> facilities = healthcareFacilityRepository.findAll();
        return facilities.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public FacilityResponse getFacilityById(UUID facilityId){
        HealthcareFacility facility = healthcareFacilityRepository.findById(facilityId).
                orElseThrow(()->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,"Facility not found")
                );
        
        return mapToResponse(facility);
    }

    public FacilityResponse updateFacility(UUID facilityId, FacilityUpdateRequest request){
        HealthcareFacility facility = healthcareFacilityRepository.findById(facilityId).
                orElseThrow(()->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,"Facility not found")
                );
        String updatedName = request.getFacilityName().trim();
        boolean nameExists = healthcareFacilityRepository.existsByFacilityNameIgnoreCase(updatedName);
        if (nameExists &&
                !facility.getFacilityName().equalsIgnoreCase(updatedName)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Another facility with this name already exists"
            );
        }

        facility.setFacilityName(updatedName);
        facility.setFacilityType(request.getFacilityType());
        facility.setAddress(request.getAddress());
        facility.setContactNumber(request.getContactNumber());

        HealthcareFacility updatedFacility =
                healthcareFacilityRepository.save(facility);

        return mapToResponse(updatedFacility);
    }

    public FacilityResponse updateFacilityStatus(UUID facilityId, @Valid FacilityStatusRequest request){
        HealthcareFacility facility = healthcareFacilityRepository.findById(facilityId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Facility Not Found")
        );
        facility.setStatus(request.getStatus());

        HealthcareFacility updatedFacility =
                healthcareFacilityRepository.save(facility);

        return mapToResponse(updatedFacility);
    }

    private FacilityResponse mapToResponse(HealthcareFacility facility) {

        return new FacilityResponse(
                facility.getFacilityId(),
                facility.getFacilityName(),
                facility.getFacilityType(),
                facility.getAddress(),
                facility.getContactNumber(),
                facility.getStatus(),
                facility.getCreatedAt(),
                facility.getUpdatedAt()
        );
    }
}