package com.socialmedia.miniinstagram.controller;

import com.socialmedia.miniinstagram.dto.PasswordUpdateRequest;
import com.socialmedia.miniinstagram.dto.PublicUserProfile;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserPublicProfile(@PathVariable Long id) {
        User user = userService.getById(id);

        PublicUserProfile dto = new PublicUserProfile(
                user.getId(),
                user.getUsername(),
                user.getProfileImage(),
                user.getPosts().size(),
                user.getCreatedAt()
        );

        return ResponseEntity.ok(dto);
    }
    BURADA İSTENİLEN ŞEY KULLANICI KENDİ PROFİLİNİ Mİ GÖRÜNTÜLESİN YOKSA HERKES BİRBİRİNİN PROFİLİNİ Mİ GÖRÜNTÜLESİN ONU ANLAYAMADIM
    ONA GÖRE BURAYI ŞEKİLLENDİRECEĞİM BENCE HERKES BİRBİRİNİN PROFİLİNİ GÖRSÜN AMAÇ BU YANİ AMA BAKALIM ŞUANLIK BURASI ????

     */


    @PutMapping("/me/password")
    public ResponseEntity<?> updatePassword(
            @RequestAttribute("currentUser") User currentUser,
            @RequestBody PasswordUpdateRequest req) {

        userService.updatePassword(currentUser.getId(), req.getOldPassword(), req.getNewPassword());
        return ResponseEntity.ok("Password updated successfully.");
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteSelf(
            @RequestAttribute("currentUser") User currentUser) {

        userService.deleteOwnAccount(currentUser.getId());
        return ResponseEntity.ok("Account deleted.");
    }
}
