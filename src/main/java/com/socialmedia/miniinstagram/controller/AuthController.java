package com.socialmedia.miniinstagram.controller;

import com.socialmedia.miniinstagram.dto.LoginRequest;
import com.socialmedia.miniinstagram.dto.LoginResponse;
import com.socialmedia.miniinstagram.dto.SignupRequest;
import com.socialmedia.miniinstagram.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        authService.signup(request);
        return "User created successfully";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authHeader) {

        if (!authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing Bearer token");
        }

        String token = authHeader.substring(7);
        authService.logout(token);
        return "Logged out successfully";
    }
}
