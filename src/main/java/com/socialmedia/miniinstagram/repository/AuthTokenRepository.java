package com.socialmedia.miniinstagram.repository;

import com.socialmedia.miniinstagram.entity.AuthToken;
import com.socialmedia.miniinstagram.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByToken(String token);
    List<AuthToken> findAllByUserAndRevokedFalse(User user);

    @Transactional
    @Modifying
    void deleteByUser(User user);
}
