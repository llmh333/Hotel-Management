package org.example.hotel_management.entity;

import org.example.hotel_management.dto.response.UserResponseDTO;

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
        return currentUser.getRole().equals("ADMIN");
    }
}
