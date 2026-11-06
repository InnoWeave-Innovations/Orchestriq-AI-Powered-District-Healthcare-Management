package com.orchestriq.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DesignationRequest(

        @NotBlank(message = "Designation name is required")
        @Size(max = 100, message = "Designation name must not exceed 100 characters")
        String designationName,

        @Size(max = 2000, message = "Description must not exceed 2000 characters")
        String description

) {
}
