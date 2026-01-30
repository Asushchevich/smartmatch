package com.smartmatch.common;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public abstract class BaseSecurityConfig {

    protected SecurityFilterChain createChain(HttpSecurity http, JwtUtils jwtUtils, String... publicPaths) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicPaths).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}