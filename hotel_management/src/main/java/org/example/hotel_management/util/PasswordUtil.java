package org.example.hotel_management.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
