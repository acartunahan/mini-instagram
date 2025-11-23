package com.socialmedia.miniinstagram.service;

import com.socialmedia.miniinstagram.dto.LoginRequest;
import com.socialmedia.miniinstagram.dto.LoginResponse;
import com.socialmedia.miniinstagram.dto.SignupRequest;
import com.socialmedia.miniinstagram.entity.AuthToken;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.model.Role;
import com.socialmedia.miniinstagram.repository.AuthTokenRepository;
import com.socialmedia.miniinstagram.repository.UserRepository;
import com.socialmedia.miniinstagram.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;
    private final TokenGenerator tokenGenerator;

    // ---------------- SIGNUP ----------------
    public User signup(SignupRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtil.hashPassword(request.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    // ---------------- LOGIN ----------------
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // Şifre doğru mu kontrol et
        if (!PasswordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Yeni token üret
        String tokenStr = tokenGenerator.generateToken();

        AuthToken token = new AuthToken();
        token.setToken(tokenStr);
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().plusHours(2)); // 2 saat geçerli

        authTokenRepository.save(token);

        return new LoginResponse(tokenStr, token.getExpiresAt().toString());
    }

    // ---------------- LOGOUT ----------------
    public void logout(String tokenStr) {

        AuthToken token = authTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        token.setRevoked(true);
        authTokenRepository.save(token);
    }
}
