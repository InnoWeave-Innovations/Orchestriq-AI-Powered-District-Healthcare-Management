
package com.orchestriq.backend.repository;

import com.orchestriq.backend.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DesignationRepository
        extends JpaRepository<Designation, UUID> {

    boolean existsByDesignationNameIgnoreCase(String designationName);
}
