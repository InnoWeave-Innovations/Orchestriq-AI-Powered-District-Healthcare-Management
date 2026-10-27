
package com.orchestriq.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "roles", schema = "public")
public class Role {

    @Id
    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "description")
    private String description;

    protected Role() {
        // Required by JPA
    }

    public UUID getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getDescription() {
        return description;
    }
}
