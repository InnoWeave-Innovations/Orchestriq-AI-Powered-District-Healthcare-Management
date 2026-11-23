
package com.orchestriq.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateUserRequest(

        @NotBlank(message = "Full name is required")
        @Size(max = 150)
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 255)
        String email,

        @NotNull(message = "Role ID is required")
        UUID roleId,

        @NotNull(message = "Designation ID is required")
        UUID designationId,

        @NotNull(message = "Facility ID is required")
        UUID facilityId

) {}
