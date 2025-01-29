package com.am.SalesConversionAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security configuration for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Because we have stateless API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/metrics").authenticated() // Protect metrics endpoint
                        .anyRequest().permitAll() // Allow all other endpoints
                )
                .httpBasic(withDefaults()); // Enable Basic Authentication

        return http.build();
    }
}
