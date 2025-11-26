package com.socialmedia.miniinstagram.service;

import com.socialmedia.miniinstagram.dto.PublicUserProfile;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.model.Role;
import com.socialmedia.miniinstagram.repository.AuthTokenRepository;
import com.socialmedia.miniinstagram.repository.UserRepository;
import com.socialmedia.miniinstagram.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);

        if (!PasswordUtil.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        String newHashed = PasswordUtil.hashPassword(newPassword);
        user.setPassword(newHashed);
        userRepository.save(user);

        authTokenRepository.deleteByUser(user);
    }

    public void deleteOwnAccount(Long userId) {
        User user = getById(userId);

        authTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    public void adminDeleteUser(Long id) {
        User user = getById(id);

        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Admin cannot delete another admin for now.");
        }

        authTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    public PublicUserProfile getPublicProfile(Long userId) {
        User user = getById(userId);

        return new PublicUserProfile(
                user.getId(),
                user.getUsername(),
                user.getPosts() != null ? user.getPosts().size() : 0,
                user.getCreatedAt()
        );
    }

}
