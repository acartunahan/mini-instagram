package com.socialmedia.miniinstagram.repository;

import com.socialmedia.miniinstagram.entity.AuthToken;
import com.socialmedia.miniinstagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByToken(String token);  //Filter’da gelen bearer token’ı bulmak için
    List<AuthToken> findAllByUserAndRevokedFalse(User user);  //Logout olduğunda aynı kullanıcıya ait tüm aktif tokenları revoke etmek için (yani hepsini kapatmak için)
}
