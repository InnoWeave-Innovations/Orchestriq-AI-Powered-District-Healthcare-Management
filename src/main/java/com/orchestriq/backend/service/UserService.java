
package com.orchestriq.backend.service;

import com.orchestriq.backend.dto.CreateUserRequest;
import com.orchestriq.backend.dto.UpdateUserRequest;
import com.orchestriq.backend.dto.UpdateUserStatusRequest;
import com.orchestriq.backend.dto.UserResponse;

import com.orchestriq.backend.entity.User;
import com.orchestriq.backend.entity.Designation;
import com.orchestriq.backend.entity.HealthcareFacility;

import com.orchestriq.backend.repository.UserRepository;
import com.orchestriq.backend.repository.RoleRepository;
import com.orchestriq.backend.repository.DesignationRepository;
import com.orchestriq.backend.repository.HealthcareFacilityRepository;

import com.orchestriq.backend.security.PasswordHashService;

import com.orchestriq.backend.exception.ResourceNotFoundException;
import com.orchestriq.backend.exception.DuplicateResourceException;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DesignationRepository designationRepository;
    private final HealthcareFacilityRepository facilityRepository;
    private final PasswordHashService passwordHashService;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            DesignationRepository designationRepository,
            HealthcareFacilityRepository facilityRepository,
            PasswordHashService passwordHashService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.designationRepository = designationRepository;
        this.facilityRepository = facilityRepository;
        this.passwordHashService = passwordHashService;
    }

    // --------------------------------------------------
    // 1. GET ALL USERS
    // --------------------------------------------------

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "createdAt")
                )
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    // --------------------------------------------------
    // 2. CREATE USER
    // --------------------------------------------------


    public UserResponse createUser(CreateUserRequest request) {

        // Get the currently logged-in user
        User creator = getAuthenticatedUser();

        String creatorRole = getRoleName(creator.getRoleId());

        // Get the role being assigned to the new user
        String targetRole = getRoleName(request.roleId());

        // Only these two roles can be created through this API
        if (!targetRole.equals("HOSPITAL_STAFF")
                && !targetRole.equals("HOSPITAL_ADMIN")) {

            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to create this user role"
            );
        }

        // DISTRICT_ADMIN can create both roles
        if (creatorRole.equals("DISTRICT_ADMIN")) {

            // District Admin can create users at any active facility

        }

        // HOSPITAL_ADMIN can create only HOSPITAL_STAFF
        else if (creatorRole.equals("HOSPITAL_ADMIN")) {

            if (!targetRole.equals("HOSPITAL_STAFF")) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "Hospital Admin can create only Hospital Staff"
                );
            }

            // Hospital Admin can create users only at their own facility
            if (creator.getFacilityId() == null
                    || !creator.getFacilityId().equals(request.facilityId())) {

                throw new org.springframework.security.access.AccessDeniedException(
                        "Hospital Admin can create users only at their own facility"
                );
            }
        }

        else {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You do not have permission to create users"
            );
        }

        String email = normalizeEmail(request.email());

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException(
                    "A user with this email already exists"
            );
        }

        validateRole(request.roleId());
        validateDesignation(request.designationId());
        validateFacility(request.facilityId());

        String passwordHash =
                passwordHashService.hashPassword(request.password());

        User user = User.create(
                request.fullName().trim(),
                email,
                passwordHash,
                request.roleId(),
                request.designationId(),
                request.facilityId(),
                creator.getUserId()
        );

        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }

    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            throw new org.springframework.security.access.AccessDeniedException(
                    "Authentication required"
            );
        }

        UUID userId;

        try {
            userId = UUID.fromString(authentication.getName());
        } catch (IllegalArgumentException exception) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Invalid authenticated user ID"
            );
        }

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Authenticated user not found: " + userId
                        )
                );
    }



    private String getRoleName(UUID roleId) {

        return roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found: " + roleId
                        )
                )
                .getRoleName()
                .toUpperCase(Locale.ROOT);
    }


    // --------------------------------------------------
    // 3. GET USER BY ID
    // --------------------------------------------------

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID userId) {

        User user = findUser(userId);

        return UserResponse.from(user);
    }

    // --------------------------------------------------
    // 4. UPDATE USER
    // --------------------------------------------------

    public UserResponse updateUser(
            UUID userId,
            UpdateUserRequest request) {

        User user = findUser(userId);

        String email = normalizeEmail(request.email());

        if (userRepository.existsByEmailIgnoreCaseAndUserIdNot(
                email,
                userId)) {

            throw new DuplicateResourceException(
                    "Another user already exists with this email"
            );
        }

        validateRole(request.roleId());

        validateDesignation(request.designationId());

        validateFacility(request.facilityId());

        user.updateDetails(
                request.fullName().trim(),
                email,
                request.roleId(),
                request.designationId(),
                request.facilityId()
        );

        User updatedUser = userRepository.save(user);

        return UserResponse.from(updatedUser);
    }

    // --------------------------------------------------
    // 5. ACTIVATE / DEACTIVATE USER
    // --------------------------------------------------

    public UserResponse updateUserStatus(
            UUID userId,
            UpdateUserStatusRequest request) {

        User user = findUser(userId);

        user.changeStatus(request.status());

        User updatedUser = userRepository.save(user);

        return UserResponse.from(updatedUser);
    }

    // --------------------------------------------------
    // 6. GET USERS BY FACILITY
    // --------------------------------------------------

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByFacility(UUID facilityId) {

        if (!facilityRepository.existsById(facilityId)) {
            throw new ResourceNotFoundException(
                    "Healthcare facility not found: " + facilityId
            );
        }

        return userRepository.findAllByFacilityId(facilityId)
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    // --------------------------------------------------
    // COMMON HELPER METHODS
    // --------------------------------------------------

    private User findUser(UUID userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + userId
                        )
                );
    }

    private void validateRole(UUID roleId) {

        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException(
                    "Role not found: " + roleId
            );
        }
    }

    private void validateDesignation(UUID designationId) {

        Designation designation = designationRepository
                .findById(designationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Designation not found: " + designationId
                        )
                );

        if (!"ACTIVE".equalsIgnoreCase(designation.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot assign an inactive designation"
            );
        }
    }

    private void validateFacility(UUID facilityId) {

        HealthcareFacility facility = facilityRepository
                .findById(facilityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Healthcare facility not found: " + facilityId
                        )
                );

        if (!"ACTIVE".equalsIgnoreCase(facility.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot assign an inactive healthcare facility"
            );
        }
    }

    private String normalizeEmail(String email) {

        return email.trim().toLowerCase(Locale.ROOT);
    }

    private UUID getAuthenticatedUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            throw new IllegalStateException(
                    "Authenticated user not found"
            );
        }

        // Assumption: JWT authentication uses email as principal name.
        String email = authentication.getName();

        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Authenticated creator not found"
                        )
                )
                .getUserId();
    }
}
