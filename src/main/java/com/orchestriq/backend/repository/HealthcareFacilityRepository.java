package com.orchestriq.backend.repository;

import com.orchestriq.backend.entity.HealthcareFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HealthcareFacilityRepository extends JpaRepository<HealthcareFacility, UUID> {
    boolean existsByFacilityNameIgnoreCase(String facilityName);
}
