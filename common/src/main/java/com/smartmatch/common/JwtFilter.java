package com.smartmatch.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.contains("/ws-notifications") || path.contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        log.info("Incoming request to: {}", request.getRequestURI());

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("No Bearer token found in request headers for: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        log.info("Token extracted from header. Length: {}", token.length());

        try {
            String username = jwtUtils.extractUsername(token);
            log.info("Username extracted from token: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<String> roles = jwtUtils.extractRoles(token);
                log.info("Roles extracted: {}", roles);

                var authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                var authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("SecurityContext successfully updated for user: {}", username);
            }
        } catch (Exception e) {
            log.error("JWT Authentication failed! Error: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}