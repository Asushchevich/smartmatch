package com.smartmatch.auth.controller;

import com.smartmatch.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/token")
    public String getToken(@RequestParam("username") String username,
                           @RequestParam("password") String password) {
        return jwtService.generateToken(username);
    }
}