
package com.orchestriq.backend.repository;

import com.orchestriq.backend.entity.RevokedToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedTokenRepository
        extends JpaRepository<RevokedToken, String> {
}
