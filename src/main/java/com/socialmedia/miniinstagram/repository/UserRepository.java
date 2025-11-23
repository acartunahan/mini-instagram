package com.socialmedia.miniinstagram.repository;

import com.socialmedia.miniinstagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); //Login’de kullanıcıyı bulmak için
    boolean existsByUsername(String username);  //Signup sırasında “username zaten var mı?” kontrolü için
}
