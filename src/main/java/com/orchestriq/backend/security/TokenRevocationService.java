
package com.orchestriq.backend.security;

import com.orchestriq.backend.entity.RevokedToken;
import com.orchestriq.backend.repository.RevokedTokenRepository;

import io.jsonwebtoken.Claims;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TokenRevocationService {

    private final JwtService jwtService;
    private final RevokedTokenRepository revokedTokenRepository;

    public TokenRevocationService(
            JwtService jwtService,
            RevokedTokenRepository revokedTokenRepository) {

        this.jwtService = jwtService;
        this.revokedTokenRepository = revokedTokenRepository;
    }

    @Transactional
    public void revokeToken(String token) {

        Claims claims = jwtService.validateAndGetClaims(token);

        String jti = claims.getId();
        Date expiration = claims.getExpiration();

        if (jti == null || jti.isBlank() || expiration == null) {
            throw new IllegalArgumentException(
                    "JWT is missing its ID or expiration"
            );
        }

        if (revokedTokenRepository.existsById(jti)) {
            return;
        }

        revokedTokenRepository.save(
                new RevokedToken(jti, expiration.toInstant())
        );
    }

    @Transactional(readOnly = true)
    public boolean isRevoked(String jti) {

        return jti != null
                && revokedTokenRepository.existsById(jti);
    }
}
