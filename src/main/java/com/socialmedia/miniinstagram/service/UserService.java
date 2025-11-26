package com.socialmedia.miniinstagram.service;

import com.socialmedia.miniinstagram.dto.PublicUserProfile;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.exception.AppException;
import com.socialmedia.miniinstagram.model.Role;
import com.socialmedia.miniinstagram.repository.AuthTokenRepository;
import com.socialmedia.miniinstagram.repository.UserRepository;
import com.socialmedia.miniinstagram.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    }

    public void updatePassword(Long userId, String oldPassword, String newPassword) {

        User user = getById(userId);

        if (!PasswordUtil.matches(oldPassword, user.getPassword())) {
            throw new AppException("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(PasswordUtil.hashPassword(newPassword));
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
            throw new AppException("Admin cannot delete another admin", HttpStatus.FORBIDDEN);
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
