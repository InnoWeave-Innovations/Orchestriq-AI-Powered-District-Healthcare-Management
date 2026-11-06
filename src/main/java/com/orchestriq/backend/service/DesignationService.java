
package com.orchestriq.backend.service;

import com.orchestriq.backend.dto.DesignationRequest;
import com.orchestriq.backend.dto.DesignationResponse;
import com.orchestriq.backend.entity.Designation;
import com.orchestriq.backend.repository.DesignationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
public class DesignationService {

    private final DesignationRepository designationRepository;

    public DesignationService(
            DesignationRepository designationRepository) {
        this.designationRepository = designationRepository;
    }

    public DesignationResponse createDesignation(
            DesignationRequest request) {

        String name = request.designationName().trim();

        if (designationRepository
                .existsByDesignationNameIgnoreCase(name)) {
            throw new ResponseStatusException(
                    CONFLICT, "Designation name already exists");
        }

        Designation designation = Designation.create(
                name,
                request.description()
        );

        Designation saved = designationRepository.save(designation);

        return DesignationResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<DesignationResponse> getAllDesignations() {
        return designationRepository.findAll()
                .stream()
                .map(DesignationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DesignationResponse getDesignationById(UUID id) {
        Designation designation = designationRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Designation not found"));

        return DesignationResponse.from(designation);
    }

    public DesignationResponse updateDesignation(
            UUID id, DesignationRequest request) {

        Designation designation = designationRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Designation not found"));

        String name = request.designationName().trim();

        boolean duplicate = designationRepository
                .existsByDesignationNameIgnoreCase(name);

        if (duplicate && !name.equalsIgnoreCase(
                designation.getDesignationName())) {
            throw new ResponseStatusException(
                    CONFLICT, "Designation name already exists");
        }

        designation.updateDetails(name, request.description());

        return DesignationResponse.from(
                designationRepository.save(designation));
    }

    public DesignationResponse changeDesignationStatus(
            UUID id, String status) {

        Designation designation = designationRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Designation not found"));

        if (!"ACTIVE".equals(status)
                && !"INACTIVE".equals(status)) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Status must be ACTIVE or INACTIVE");
        }

        designation.changeStatus(status);

        return DesignationResponse.from(
                designationRepository.save(designation));
    }
}
