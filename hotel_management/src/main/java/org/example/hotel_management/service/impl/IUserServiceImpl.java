package org.example.hotel_management.service.impl;

import org.example.hotel_management.dao.UserDAO;
import org.example.hotel_management.dto.request.ChangePasswordRequestDTO;
import org.example.hotel_management.dto.request.UpdateProfileRequestDTO;
import org.example.hotel_management.dto.response.UserResponseDTO;
import org.example.hotel_management.entity.User;
import org.example.hotel_management.mapper.UserMapper;
import org.example.hotel_management.service.IUserService;
import org.example.hotel_management.util.PasswordUtil;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.logging.Logger;

public class IUserServiceImpl implements IUserService {

    private static final IUserServiceImpl INSTANCE = new IUserServiceImpl();
    private final UserMapper userMapper = UserMapper.getInstance();
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

    @Override
    public boolean changePassword(ChangePasswordRequestDTO request) {
        Optional<User> userOptional = userDAO.findById(request.getId());
        if (userOptional.isEmpty()) return false;
        User user = userOptional.get();
        user.setPassword(PasswordUtil.encode(request.getNewPassword()));
        Optional<User> userUpdateOptional = userDAO.update(user);
        return userUpdateOptional.isPresent();
    }

    @Override
    public UserResponseDTO updateProfile(UpdateProfileRequestDTO request) {

        Optional<User> userOptional = userDAO.findById(request.getId());
        if (userOptional.isEmpty()) return null;
        User user = userOptional.get();

        if (!request.getNewFullName().equals(user.getFullName())) {
            user.setFullName(request.getNewFullName());
        }
        if (!request.getNewPhoneNumber().equals(user.getPhoneNumber())) {
            user.setPhoneNumber(request.getNewPhoneNumber());
        }

        Optional<User> userUpdateOptional = userDAO.update(user);
        UserResponseDTO userResponseDTO = userUpdateOptional.map(userMapper::toUserResponseDTO).orElse(null);
        return userResponseDTO;
    }
}
