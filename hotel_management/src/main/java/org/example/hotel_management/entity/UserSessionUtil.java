package org.example.hotel_management.entity;

import org.example.hotel_management.dto.response.UserResponseDTO;
import org.example.hotel_management.enums.Role;

public class UserSessionUtil {

    private static UserSessionUtil INSTANCE;
    private UserResponseDTO currentUser;

    private UserSessionUtil() {}

    public static synchronized UserSessionUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserSessionUtil();
        }
        return INSTANCE;
    }

    public UserResponseDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserResponseDTO user) {
        this.currentUser = user;
    }

    public void clearSession() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return Role.ADMIN.equals(currentUser.getRole());
    }
}
