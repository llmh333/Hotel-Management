package org.example.hotel_management.service.impl;

import org.example.hotel_management.dao.UserDAO;
import org.example.hotel_management.entity.User;
import org.example.hotel_management.service.IUserService;
import org.example.hotel_management.util.PasswordUtil;

import java.util.Optional;
import java.util.logging.Logger;

public class IUserServiceImpl implements IUserService {

    private static final IUserServiceImpl INSTANCE = new IUserServiceImpl();
    private final UserDAO userDAO = UserDAO.getInstance();

    private final Logger logger = Logger.getLogger(IUserServiceImpl.class.getName());

    public static IUserServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        if (newPassword == null || oldPassword == null || email == null) return false;

        logger.info("Changing password for user: " + email);
        Optional<User> userOptional = userDAO.findByEmail(email);
        if (userOptional.isEmpty()) return false;
        User user = userOptional.get();
        user.setPassword(PasswordUtil.encode(newPassword));
        Optional<User> userUpdateOptional = userDAO.update(user);

        if (userUpdateOptional.isEmpty()) return false;
        return true;
    }
}
