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

        // Auth endpointleri için token kontrol etmiyoruz
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth")) {
            chain.doFilter(request, response);
            return;
        }

        // Authorization header yoksa → 401
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(401, "Missing or invalid Authorization header");
            return;
        }

        String tokenStr = authHeader.substring(7);

        // Token veritabanında var mı?
        AuthToken token = authTokenRepository.findByToken(tokenStr)
                .orElse(null);

        if (token == null) {
            response.sendError(401, "Invalid token");
            return;
        }

        // Token revoked mu?
        if (token.isRevoked()) {
            response.sendError(401, "Token revoked");
            return;
        }

        // Token süresi bitmiş mi?
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            response.sendError(401, "Token expired");
            return;
        }

        // Token geçerli → request normal devam eder
        chain.doFilter(request, response);
    }
}
