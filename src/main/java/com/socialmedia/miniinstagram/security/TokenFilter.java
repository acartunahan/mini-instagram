package com.socialmedia.miniinstagram.security;

import com.socialmedia.miniinstagram.entity.AuthToken;
import com.socialmedia.miniinstagram.repository.AuthTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TokenFilter extends HttpFilter {

    private final AuthTokenRepository authTokenRepository;

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)
            throws IOException, ServletException {

        String path = request.getRequestURI();

        if (path.equals("/api/auth/signup") || path.equals("/api/auth/login")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(401, "Missing or invalid Authorization header");
            return;
        }

        String tokenStr = authHeader.substring(7);

        AuthToken token = authTokenRepository.findByToken(tokenStr)
                .orElse(null);

        if (token == null) {
            response.sendError(401, "Invalid token");
            return;
        }

        if (token.isRevoked()) {
            response.sendError(401, "Token revoked");
            return;
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            response.sendError(401, "Token expired");   
            return;
        }

        var user = token.getUser();
        request.setAttribute("currentUser", user); //

        chain.doFilter(request, response);
    }
}
