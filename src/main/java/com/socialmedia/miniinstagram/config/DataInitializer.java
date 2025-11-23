package com.socialmedia.miniinstagram.config;

import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.model.Role;
import com.socialmedia.miniinstagram.repository.UserRepository;
import com.socialmedia.miniinstagram.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {

        String adminUsername = "admin";
        String adminPassword = "admin123";

        boolean adminExists = userRepository.findByUsername(adminUsername).isPresent();

        if (!adminExists) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(PasswordUtil.hashPassword(adminPassword));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("✅ Default ADMIN user created: " + adminUsername + " / " + adminPassword);
        } else {
            System.out.println("ℹ️ ADMIN user already exists, skipping creation.");
        }
    }
}
