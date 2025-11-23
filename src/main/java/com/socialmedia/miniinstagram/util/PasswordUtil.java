package com.socialmedia.miniinstagram.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Şifreyi hash'le
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10)); // cost=10 yeterli
    }

    // Düz şifre, hash'lenmiş şifreyle eşleşiyor mu?
    public static boolean matches(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
