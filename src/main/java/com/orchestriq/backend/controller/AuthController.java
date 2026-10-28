
package com.orchestriq.backend.controller;

import com.orchestriq.backend.dto.LoginRequest;
import com.orchestriq.backend.dto.LoginResponse;
import com.orchestriq.backend.service.AuthService;
import com.orchestriq.backend.security.TokenRevocationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenRevocationService tokenRevocationService;

    public AuthController(
            AuthService authService,
            TokenRevocationService tokenRevocationService) {

        this.authService = authService;
        this.tokenRevocationService = tokenRevocationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            ) String authorizationHeader) {

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Missing Bearer token");
        }

        String token = authorizationHeader.substring(7).trim();

        if (token.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Missing Bearer token");
        }

        try {
            tokenRevocationService.revokeToken(token);

            return ResponseEntity.ok("Logout successful");

        } catch (io.jsonwebtoken.JwtException
                 | IllegalArgumentException exception) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }
}
