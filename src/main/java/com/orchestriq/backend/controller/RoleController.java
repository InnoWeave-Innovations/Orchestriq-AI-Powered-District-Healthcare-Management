package com.orchestriq.backend.controller;

import com.orchestriq.backend.dto.RoleResponse;
import com.orchestriq.backend.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }
    @GetMapping
    @PreAuthorize("hasRole('DISTRICT_ADMIN')")
    public List<RoleResponse> getAllRoles(){
        return roleService.getAllRoles();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DISTRICT_ADMIN')")
    public RoleResponse getRoleById(@PathVariable UUID id) {
        return roleService.getRoleById(id);
    }
}
