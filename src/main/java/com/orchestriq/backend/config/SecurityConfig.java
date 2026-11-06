package com.orchestriq.backend.config;

import com.orchestriq.backend.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) ->
                                response.sendError(
                                        jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED,
                                        "Unauthorized"
                                )
                        )
                        .accessDeniedHandler((request, response, exception) ->
                                response.sendError(
                                        jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN,
                                        "Forbidden"
                                )
                        )
                )
                .authorizeHttpRequests(auth -> auth

                        // Public authentication endpoint
                        .requestMatchers("/api/v1/auth/login").permitAll()

                        // Designation creation - District Admin only
                        .requestMatchers(
                                org.springframework.http.HttpMethod.POST,
                                "/api/v1/designations"
                        ).hasRole("DISTRICT_ADMIN")

                        // Designation update - District Admin only
                        .requestMatchers(
                                org.springframework.http.HttpMethod.PUT,
                                "/api/v1/designations/**"
                        ).hasRole("DISTRICT_ADMIN")

                        // Designation status change - District Admin only
                        .requestMatchers(
                                org.springframework.http.HttpMethod.PATCH,
                                "/api/v1/designations/**"
                        ).hasRole("DISTRICT_ADMIN")

                        // All remaining authenticated endpoints
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

}