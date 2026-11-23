
package com.orchestriq.backend.config;

import com.orchestriq.backend.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
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
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

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

                        // Public login
                        .requestMatchers("/api/v1/auth/login")
                        .permitAll()

                        // User Management - District Admin only
                        // User creation - District Admin and Hospital Admin
                        .requestMatchers(
                                HttpMethod.POST,
                                 "/api/v1/users"
                                ).hasAnyRole("DISTRICT_ADMIN", "HOSPITAL_ADMIN")

                        // All other User Management operations - District Admin only
                        .requestMatchers(
                                "/api/v1/users",
                                        "/api/v1/users/**",
                                        "/api/v1/facilities/*/users"
                                ).hasRole("DISTRICT_ADMIN")

                        // Designation creation
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/designations"
                        )
                        .hasRole("DISTRICT_ADMIN")

                        // Designation update
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/designations/**"
                        )
                        .hasRole("DISTRICT_ADMIN")

                        // Designation status
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/designations/**"
                        )
                        .hasRole("DISTRICT_ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
