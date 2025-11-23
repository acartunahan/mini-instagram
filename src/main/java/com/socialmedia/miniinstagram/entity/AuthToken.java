package com.socialmedia.miniinstagram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "auth_tokens")
public class AuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String token;

    // Birden çok token bir user'a ait olabilir hem telden hem pc den falan girer.
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Burada service katmanında buna mesela token.setExpiresAt(LocalDateTime.now().plusHours(2)); diyeceğiz ve 2 saat geçerli olmasını sağlayacak.
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // Kullanıcı logout olursa bunu tetikliceksin if (token.isRevoked())  token iptal edilmiş → 401
    @Column(nullable = false)
    private boolean revoked = false;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
