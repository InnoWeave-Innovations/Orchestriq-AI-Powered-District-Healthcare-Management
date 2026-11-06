package com.orchestriq.backend.dto;

import java.util.UUID;

public record RoleResponse(UUID roleId,
        String roleName,
        String description) {

}
