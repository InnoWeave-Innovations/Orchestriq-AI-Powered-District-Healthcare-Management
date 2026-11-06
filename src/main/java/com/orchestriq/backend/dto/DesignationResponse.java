
package com.orchestriq.backend.dto;

import com.orchestriq.backend.entity.Designation;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DesignationResponse(
        UUID designationId,
        String designationName,
        String description,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static DesignationResponse from(Designation designation) {
        return new DesignationResponse(
                designation.getDesignationId(),
                designation.getDesignationName(),
                designation.getDescription(),
                designation.getStatus(),
                designation.getCreatedAt(),
                designation.getUpdatedAt()
        );
    }
}
