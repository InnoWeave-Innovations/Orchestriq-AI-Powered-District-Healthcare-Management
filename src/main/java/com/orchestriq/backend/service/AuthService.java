
package com.orchestriq.backend.service;

import com.orchestriq.backend.dto.LoginRequest;
import com.orchestriq.backend.dto.LoginResponse;
import com.orchestriq.backend.entity.Role;
import com.orchestriq.backend.entity.User;
import com.orchestriq.backend.repository.RoleRepository;
import com.orchestriq.backend.repository.UserRepository;
import com.orchestriq.backend.security.JwtService;
import com.orchestriq.backend.security.PasswordHashService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordHashService passwordHashService;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordHashService passwordHashService,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordHashService = passwordHashService;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));

        if (!passwordHashService.matches(
                request.password(),
                user.getPasswordHash())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Account is not active"
            );
        }

        Role role = roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "User role configuration is invalid"
                ));

        String token = jwtService.generateToken(
                user.getUserId(),
                role.getRoleName()
        );

        return new LoginResponse(
                token,
                "Bearer",
                900,
                user.getUserId(),
                user.getFullName(),
                role.getRoleName()
        );
    }
}
