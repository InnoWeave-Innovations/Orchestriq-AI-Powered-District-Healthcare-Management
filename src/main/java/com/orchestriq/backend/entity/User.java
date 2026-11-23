
package com.orchestriq.backend.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "designation_id")
    private UUID designationId;

    @Column(name = "facility_id")
    private UUID facilityId;

    @Column(name = "status")
    private String status;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    protected User() {
    }

    public static User create(
            String fullName,
            String email,
            String passwordHash,
            UUID roleId,
            UUID designationId,
            UUID facilityId,
            UUID createdBy) {

        User user = new User();

        user.userId = UUID.randomUUID();
        user.fullName = fullName;
        user.email = email;
        user.passwordHash = passwordHash;
        user.roleId = roleId;
        user.designationId = designationId;
        user.facilityId = facilityId;
        user.status = "ACTIVE";
        user.createdBy = createdBy;

        return user;
    }

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();

        if (userId == null) {
            userId = UUID.randomUUID();
        }

        if (status == null) {
            status = "ACTIVE";
        }

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public void updateDetails(
            String fullName,
            String email,
            UUID roleId,
            UUID designationId,
            UUID facilityId) {

        this.fullName = fullName;
        this.email = email;
        this.roleId = roleId;
        this.designationId = designationId;
        this.facilityId = facilityId;
    }

    public void changeStatus(String status) {
        this.status = status;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public UUID getDesignationId() {
        return designationId;
    }

    public UUID getFacilityId() {
        return facilityId;
    }

    public String getStatus() {
        return status;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
