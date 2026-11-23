
package com.orchestriq.backend.dto;

import com.orchestriq.backend.entity.User;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(

        UUID userId,
        String fullName,
        String email,
        UUID roleId,
        UUID designationId,
        UUID facilityId,
        String status,
        UUID createdBy,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt

) {

    public static UserResponse from(User user) {

        return new UserResponse(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getRoleId(),
                user.getDesignationId(),
                user.getFacilityId(),
                user.getStatus(),
                user.getCreatedBy(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
