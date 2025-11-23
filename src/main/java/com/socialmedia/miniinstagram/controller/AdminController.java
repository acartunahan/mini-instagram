package com.socialmedia.miniinstagram.controller;

import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.model.Role;
import com.socialmedia.miniinstagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(
            @RequestAttribute("currentUser") User currentUser,
            @PathVariable Long id) {

        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Only ADMIN can delete users.");
        }

        userService.adminDeleteUser(id);
        return ResponseEntity.ok("User deleted by admin.");
    }
}
