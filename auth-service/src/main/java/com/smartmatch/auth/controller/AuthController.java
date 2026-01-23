package com.smartmatch.auth.controller;

import com.smartmatch.auth.dto.AuthRequest;
import com.smartmatch.auth.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public String getToken(@RequestParam("username") String username,
                           @RequestParam("password") String password) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        if (authenticate.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
            return jwtService.generateToken(userDetails);
        } else {
            throw new RuntimeException("Invalid access");
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authenticate.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
            return jwtService.generateToken(userDetails);
        } else {
            throw new RuntimeException("Invalid access");
        }
    }
}