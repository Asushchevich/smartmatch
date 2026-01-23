package com.smartmatch.auth.service;

import com.smartmatch.common.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtils jwtUtils;

    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        System.out.println("DEBUG: Создаем токен для: " + userDetails.getUsername());
        System.out.println("DEBUG: Список ролей из БД: " + roles);

        return jwtUtils.generateToken(userDetails.getUsername(), roles);
    }
}