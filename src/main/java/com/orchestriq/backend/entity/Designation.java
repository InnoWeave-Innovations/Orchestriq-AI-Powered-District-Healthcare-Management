
package com.orchestriq.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "designations", schema = "public")
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "designation_id", nullable = false)
    private UUID designationId;

    @Column(name = "designation_name", nullable = false, unique = true, length = 100)
    private String designationName;

    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false, length = 10)
    private String status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Designation() {
        // Required by JPA
    }

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();

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

    public UUID getDesignationId() {
        return designationId;
    }

    public String getDesignationName() {
        return designationName;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updateDetails(String designationName, String description) {
        this.designationName = designationName;
        this.description = description;
    }

    public void changeStatus(String status) {
        this.status = status;
    }

    public static Designation create(
            String designationName,
            String description) {

        Designation designation = new Designation();

        designation.designationName = designationName;
        designation.description = description;
        designation.status = "ACTIVE";

        return designation;
    }


}
