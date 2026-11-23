
package com.orchestriq.backend.controller;

import com.orchestriq.backend.dto.CreateUserRequest;
import com.orchestriq.backend.dto.UpdateUserRequest;
import com.orchestriq.backend.dto.UpdateUserStatusRequest;
import com.orchestriq.backend.dto.UserResponse;

import com.orchestriq.backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --------------------------------------------------
    // 1. GET /users
    // --------------------------------------------------

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    // --------------------------------------------------
    // 2. POST /users
    // --------------------------------------------------

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        UserResponse response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // --------------------------------------------------
    // 3. GET /users/{id}
    // --------------------------------------------------

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable UUID id) {

        UserResponse response = userService.getUserById(id);

        return ResponseEntity.ok(response);
    }

    // --------------------------------------------------
    // 4. PUT /users/{id}
    // --------------------------------------------------

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {

        UserResponse response = userService.updateUser(id, request);

        return ResponseEntity.ok(response);
    }

    // --------------------------------------------------
    // 5. PATCH /users/{id}/status
    // --------------------------------------------------

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<UserResponse> updateUserStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserStatusRequest request) {

        UserResponse response =
                userService.updateUserStatus(id, request);

        return ResponseEntity.ok(response);
    }

    // --------------------------------------------------
    // 6. GET /facilities/{id}/users
    // --------------------------------------------------

    @GetMapping("/facilities/{id}/users")
    public ResponseEntity<List<UserResponse>> getUsersByFacility(
            @PathVariable UUID id) {

        List<UserResponse> users =
                userService.getUsersByFacility(id);

        return ResponseEntity.ok(users);
    }
}
