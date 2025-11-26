package com.socialmedia.miniinstagram.service;

import com.socialmedia.miniinstagram.dto.LoginRequest;
import com.socialmedia.miniinstagram.dto.LoginResponse;
import com.socialmedia.miniinstagram.dto.SignupRequest;
import com.socialmedia.miniinstagram.entity.AuthToken;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.exception.AppException;
import com.socialmedia.miniinstagram.model.Role;
import com.socialmedia.miniinstagram.repository.AuthTokenRepository;
import com.socialmedia.miniinstagram.repository.UserRepository;
import com.socialmedia.miniinstagram.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;
    private final TokenGenerator tokenGenerator;

    public User signup(SignupRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException("Username already exists", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtil.hashPassword(request.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException("Invalid username or password", HttpStatus.UNAUTHORIZED));

        if (!PasswordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new AppException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        String tokenStr = tokenGenerator.generateToken();

        AuthToken token = new AuthToken();
        token.setToken(tokenStr);
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().plusHours(2));
        authTokenRepository.save(token);

        return new LoginResponse(tokenStr, token.getExpiresAt().toString());
    }

    public void logout(String tokenStr) {

        AuthToken token = authTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new AppException("Invalid token", HttpStatus.UNAUTHORIZED));

        token.setRevoked(true);
        authTokenRepository.save(token);
    }
}
