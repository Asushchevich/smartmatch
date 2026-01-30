package com.smartmatch.match_service.config;

import com.smartmatch.common.BaseSecurityConfig;
import com.smartmatch.common.JwtFilter;
import com.smartmatch.common.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends BaseSecurityConfig {

    private final JwtUtils jwtUtils;

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtils);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtils jwtUtils) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/matches/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/matches/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/matches/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/matches/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/matches/**").hasAnyRole("ADMIN", "USER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(new com.smartmatch.common.JwtFilter(jwtUtils),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}