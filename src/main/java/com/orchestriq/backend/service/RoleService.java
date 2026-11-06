package com.orchestriq.backend.service;

import com.orchestriq.backend.dto.RoleResponse;
import com.orchestriq.backend.entity.Role;
import com.orchestriq.backend.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public List<RoleResponse> getAllRoles(){
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(this::mapToResponse).toList();
    }

    public RoleResponse getRoleById(UUID id){
        Role role = roleRepository.findById(id).orElseThrow(
            ()->
                new ResponseStatusException(
                HttpStatus.NOT_FOUND,"Role Not Found"
            )
        );
        return mapToResponse(role);
    }

    private RoleResponse mapToResponse(Role role) {

        return new RoleResponse(
                role.getRoleId(),
                role.getRoleName(),
                role.getDescription()
        );
    }
}
